import java.util.*;
import java.sql.*;
import java.io.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;

public class Ticket {
    protected static List<String[]> newAndUpdatedHires;
    protected static List<String[]> createSSQETicket;
    protected static List<String[]> createITOpsTicket;
    protected static List<String[]> missingFieldsEmailHR;
    protected static List<String[]> failedCreationsEmailAdmin = new ArrayList<>();
    protected static String username = "jeanmin_sin@scoot-tigerair.com";
    protected static String password = "QULeyTk5!@3pL</F";

    /* This class does the following:
    1. Take existing ArrayList of String[] of employee freshly pulled from WorkDay
    2. Filter by their validation statuses into respective arraylists
    3. Check if there's anymore failed creations that is lesser than 30 attempts. If there is, will add into their respective arraylists
    4. If attempt more than 30 and has not been emailed out yet, will email, and update db to emailed
    5. Create tickets for those respective arraylist and update db
    6. Those with validation statuses = 0 will be collated and emailed to HR */
    
    protected static void ticketController(List<String[]> hires){
        newAndUpdatedHires = hires;
        
        createSSQETicket = shortlist(12, "1");
        System.out.println("createSSQETicket size is " + createSSQETicket.size());
        for (String[] test:createSSQETicket){
            System.out.println(test[0]);
        }
        
        createITOpsTicket = shortlist(13, "1");
        System.out.println("createITOpsTicket size is " + createITOpsTicket.size());
        for (String[] test:createITOpsTicket){
            System.out.println(test[0]);
        }
        
        missingFieldsEmailHR = shortlist(12, "0"); 
        List <String []> result = shortlist(13, "0");
        for (String[] str : result){
            if (!missingFieldsEmailHR.contains(str)){
                missingFieldsEmailHR.add(str);
            }    
        } 
        System.out.println("missingFieldsEmailHR size is " + missingFieldsEmailHR.size());
        for (String[] test:missingFieldsEmailHR){
            System.out.println(test[0]);
        }
        
        removeHiresWithCreatedTicket(createSSQETicket, "ssqe");
        System.out.println("removeHiresWithCreatedTicket createSSQETicket size is " + createSSQETicket.size());
        for (String[] test:createSSQETicket){
            System.out.println(test[0]);
        }
        
        removeHiresWithCreatedTicket(createITOpsTicket, "itops");
        System.out.println("removeHiresWithCreatedTicket createITOpsTicket size is " + createITOpsTicket.size());
        for (String[] test:createITOpsTicket){
            System.out.println(test[0]);
        }
        
        //these are requests that don't appear in the custom daily workday report anymore, but failed ticket creation request 
        addRemainingFailedRequests(createSSQETicket, "ssqe");
        System.out.println("addRemainingFailedRequests createSSQETicket size is " + createSSQETicket.size());
        for (String[] test:createSSQETicket){
            System.out.println(test[0]);
        }
        
        addRemainingFailedRequests(createITOpsTicket, "itops");
        System.out.println("addRemainingFailedRequests createITOpsTicket size is " + createITOpsTicket.size());
        for (String[] test:createITOpsTicket){
            System.out.println(test[0]);
        }

        processAndValidateRequest("ssqe", "ssqe", createSSQETicket);
        
        processAndValidateRequest("itops", "itops", createITOpsTicket);
        
        System.out.println("!@#$%^&*(^$#@)(*&^$#@");
        
        for (String[] str : failedCreationsEmailAdmin){
            System.out.println(str[0]);
        }
        
        System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
        
        for (String[] test:createSSQETicket){
            System.out.println(test[0]);
        }
        
        processFailedCreations();
        processMissingFields();
    }
    
    //Method takes in the ArrayList of String[] of employees details
    //Shortlists them by their validation (i.e. if SSQE_validation is 1, it means all the fields SSQE requires for processing are not NULL/empty)
    //As such, it is then shortlisted by shortlist(12, 1)
    //index/columnNo 12 is SSQE_validation
    //index/columnNo 13 is itOps_validation
    //Status is for either 1 or 0, 1 being true and 0 being false
    //The returned ArrayList will be used to create their respective tickets
    protected static List<String[]> shortlist (int columnNo, String status){
        List<String[]> result = new ArrayList<>();
        for (String[] str : newAndUpdatedHires){
            if (str[columnNo].equals(status)){
                result.add(str);
            }
        }
        return result;
    }
    
    //Method takes in List returned by shortlist()
    //Removes String[] of employee whom already have their tickets created (they will still appear in the Workday Custom API if their hire date != today's date or HR updates their fields)
    //I.e. when the employee_ID + hiredate + ticket_type is used to query and it returns a ticket_number or ticket_id -> Already have created tickets
    //Helps to prevent duplicate creation of tickets
    protected static void removeHiresWithCreatedTicket(List<String[]> list, String type){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<String[]> extractedData = new ArrayList<>();
        String[] row = new String[6];
        try(Connection connection = ConnectionManager.getConnection()){
            for (int i = list.size()-1; i >= 0; i--){
                String[] str =  list.get(i);
                String empID = str[0];
                String hireDate = str[9];
                String statement = "SELECT * FROM wsis.tickets where employee_ID = '" + empID + "' and hire_date = '" + hireDate + "' AND ticket_type = '" + type + "';";
                //System.out.println(statement);
                ps = connection.prepareStatement(statement);
                resultSet = ps.executeQuery();
                
                while (resultSet.next()){
                    for (int j = 1; j <= row.length; j++){
                        row[j-1] = resultSet.getString(j);
                        //System.out.println(row[j-1]);
                    }
                    if (row[row.length-1] != null){ 
                        // means created
                        list.remove(i);
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    //Method searches the database to see if there's anymore failed request on the indicated ticket type that are 
    //not in workday API (i.e. newAndUpdatedHires) - presumably because they're not updated by HR anymore, and failed to create on ServiceNow
    //but is inside the database
    //If the creation_attempts is more than 30, and email_admin_if_fail in db is false, then it will all be accumulated for emailing later
    //Updates the respective List (e.g. createITOpsTicket)
    public static void addRemainingFailedRequests (List<String[]> list, String type){
        //this is to find and those who failed and doesn't appear in the daily workday report anymore
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<String[]> result = new ArrayList<>();
        String[] row = new String [7];
        try (Connection connection = ConnectionManager.getConnection()){
            String statement = "SELECT * FROM wsis.tickets where ticket_number IS null AND ticket_id IS null AND ticket_type = '" + type + "';";
            //System.out.println(statement);
            ps = connection.prepareStatement(statement);
            resultSet = ps.executeQuery();
            while (resultSet.next()){
                for (int i = 1; i <= row.length; i++){
                    row[i-1] = resultSet.getString(i);
                }
                
                boolean duplicate = false;
                int attempts = Integer.parseInt(row[3]);
                
                for (int i = list.size()-1; i >= 0; i--){
                    String[] str = list.get(i);
                    System.out.println("EXTRACTED: " + row[0] + "    " + row[2]);
                    System.out.println("INSIDE LIST: " + str[0] + "    " + str[2]);
                    if (str[0].equals(row[0])){
                        duplicate = true;
                        System.out.println("DUPLICATE: TRUE" + str[0] + "    " + row[0]);
                    }
                    
                    if (attempts >= 30){
                        failedCreationsEmailAdmin.add(row);
                        if (duplicate){
                            list.remove(i);
                        }
                    }
                }
                
                if (!duplicate){
                    result.add(row);
                }
                row = new String [7];
            }

            for (String[] str : result){
                row = new String [16];
                String empID = str[0];
                String hireDate = str[1];
                statement = "SELECT * FROM wsis.workday_report where employee_ID = " + empID + " and hire_date = '" + hireDate + "';";
                //System.out.println(statement);
                ps = connection.prepareStatement(statement);
                resultSet = ps.executeQuery();
                while (resultSet.next()){
                    for (int i = 1; i <= row.length; i++){
                        row[i-1] = resultSet.getString(i);
                    }
                    list.add(row);
                }
            }
        }catch(SQLException e){
            e.getMessage();
        }catch(Exception e){
            e.getMessage();
        }
        
    }
    
    //Validates processAndValidateRequest and updates the logger accordingly, or email HR if necessary
    //type is either itops, ssqe or checkout
    //ticketType is either itops, ssqe
    protected static void processAndValidateRequest (String type, String ticketType, List<String[]> list){
        
        for (String[] hireDetails : list){
            String hireString = "";
            for (int i = 0 ; i < hireDetails.length-4; i++){
                if (i == 1){
                    if (Integer.parseInt(hireDetails[i]) == 1){
                        hireString += "rehire    ";
                    }else if (Integer.parseInt(hireDetails[i]) == 0){
                        hireString += "new hire    ";
                    }
                }else{
                    hireString += hireDetails[i] + "    ";        
                }
            }
            String postData = "";
            String url = "";
            if (type.equals("ssqe")){
                postData = "{\"sysparm_quantity\":\"1\",\"variables\":{\"Network_folder_to_be_granted\":\"" + hireString + "\",\"first_approver\":\"6a33af08dbd4a200bddfda9ebf96199a\"}}";
                url = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/items/7bc99a9ddb367a00c4b95040cf9619f0/add_to_cart";
            }else if(type.equals("itops")){
                postData = "{\"sysparm_quantity\":\"1\",\"variables\":{\"Network_folder_to_be_granted\":\"" + hireString + "\",\"first_approver\":\"2a0fb94ddb9ca2001c293fa0cf9619ff\"}}";
                url = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/items/52b5f1270fe11600198288cce1050e89/add_to_cart";
            }else{
                url = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/cart/checkout";
            }
            
            try{
                String [] result = createRequest(postData, url, hireDetails);
                int status = Integer.parseInt(result[0]);
                int countAttempts = 0; //stop at 5
                while (status != 200 && countAttempts < 5){
                    result = createRequest(postData, url, hireDetails);
                    status = Integer.parseInt(result[0]);
                    countAttempts++;
                }
                
                if (status == 200){
                    if (!type.equals("checkout")){
                        ArrayList<String[]> forCheckoutRecursive = new ArrayList<>();
                        forCheckoutRecursive.add(hireDetails);
                        processAndValidateRequest ("checkout", ticketType, forCheckoutRecursive);
                    }else{
                        String empID = hireDetails[0];
                        String hireDate = hireDetails[9];
                        
                        Logs.writeTo(empID, "ServiceNow request", "ticket creation", ticketType, "successful", "" +status);
                        
                        Connection connection = ConnectionManager.getConnection();
                        
                        
                        String statement = "SELECT * FROM wsis.tickets where employee_ID = '" + empID + "' and hire_date = '" + hireDate + "' and ticket_type = '" + ticketType + "';";
                        //System.out.println(statement);
                        PreparedStatement ps = connection. prepareStatement(statement);
                        ResultSet rs = ps.executeQuery();
                        
                        String ticketNum = result[1];
                        String ticketID = result[2];
                        
                        if (rs.next()){
                            int creationAttempts = rs.getInt(4);
                            creationAttempts++;
                            statement = "UPDATE wsis.tickets SET creation_attempts = " + creationAttempts + ", ticket_number = '" + ticketNum + "', ticket_id = '" + ticketID + "' WHERE employee_ID = '" + empID + "' AND hire_date = '" + hireDate + "' AND ticket_type = '" + ticketType + "';";
                            System.out.println("================" +statement);
                            ps = connection.prepareStatement(statement);
                            ps.executeUpdate();
                            
                        }else{
                            statement = "INSERT INTO wsis.tickets VALUES ('" + empID + "','" + hireDate + "','" + ticketType + "',1,'false','" + ticketNum + "','" + ticketID + "');";
                            System.out.println("@@@@@@@@" + statement);
                            ps = connection.prepareStatement(statement);
                            ps.executeUpdate();
                        }
                    }
                }else{
                    String empID = hireDetails[0];
                    Logs.writeTo(empID, "ServiceNow request", "ticket creation", ticketType, "failure", "" + status);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    protected static String[] createRequest (String postData, String url, String[] hireDetails) throws IOException, HttpException{
        String[] result = new String [3];
        //index 0 = http status number
        //index 1 = ticket number (if status = 200)
        //index 2 = ticket id (if status = 200)
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials creds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(AuthScope.ANY, creds);
        
        PostMethod method = new PostMethod(url);
        
        method.addRequestHeader("Accept", "application/json");
        method.addRequestHeader("Content-Type", "application/json");
        method.setRequestEntity(new ByteArrayRequestEntity(postData.getBytes()));
        
        int status = client.executeMethod(method);
        result[0] = "" + status;
        
        if (url.equals("https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/cart/checkout")){
            BufferedReader rd = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
            String line = "";
            line = rd.readLine();
            String[] array = line.split("\"");
            //array[5] is request number
            result[1] = array[5];
            //array[9] is request id
            result[2] = array[9];
        }
        
        return result;
    }
    
    protected static void processFailedCreations(){
        List <String[]> toEmail = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = null;
            for (int i = failedCreationsEmailAdmin.size()-1; i >= 0; i--){
                String[] failedCreation = failedCreationsEmailAdmin.get(i);
                if (failedCreation[4].equals("false")){
                    String[] extractedData = new String[17];
                    String empID = failedCreation[0];
                    String hireDate = failedCreation[1];
                    String ticketType = failedCreation[2];
                    ps = connection.prepareStatement("SELECT * FROM wsis.workday_report WHERE employee_ID = ? AND hire_date = ?");
                    ps.setString(1, empID);
                    ps.setString(2, hireDate);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        for (int j = 1; j < extractedData.length; j++){
                            extractedData[j-1] = rs.getString(j);
                        }
                        toEmail.add(extractedData); 
                        extractedData[16] = ticketType;
                        extractedData = new String [17];
                    }
                    Logs.writeTo(empID, "ServiceNow request", "Ticket creation attempt failed >= 30 times. Emailed System Administrator to manually process ticket.", ticketType, "successful", "");

                    ps = connection.prepareStatement("UPDATE wsis.tickets SET email_admin_if_fail = 'true' WHERE employee_ID = " + empID + " AND hire_date = '" + hireDate + "' AND ticket_type = " + ticketType + ";");
                    ps.executeUpdate();
                }
            }
            if (toEmail.isEmpty()){
                return;
            }
            Email.sendEmail(toEmail, "jeanmin.sin@hotmail.com", 2);
            System.out.println("APPLESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            System.out.println("process failed creation size is " + toEmail.size());
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    protected static void processMissingFields(){
        if (missingFieldsEmailHR.isEmpty()){
            return;
        }
        Email.sendEmail(missingFieldsEmailHR, "jeanmin.sin@hotmail.com", 1);
        for (String[] missingFields : missingFieldsEmailHR){
            String empID = missingFields[0];
            Logs.writeTo(empID, "email", "missing fields required for ticket creation. emailed HR", "","successful","");
        }
    }
    
    public static void main (String[]args){
        List<String[]> list = new ArrayList<>();
        String[] test = new String[16];
        test[0] = "0001302";
        test[1] = "1";
        test[2] = "Jing Yi Esther";
        test[3] = "Seet";
        test[4] = "Seet Jing Yi Esther";
        test[5] = "BAH SFIN001 - Finance";
        test[6] = "Corporate Finance & Reporting (Karen Goh (0000027))";
        test[7] = "SC_CORP_FIN";
        test[8] = "Fixed Term (Fixed Term)";
        test[9] = "2017-05-18-07:00";
        test[10] = "Officer, Finance";
        test[11] = "esther21121996@gmail.com";
        test[12] = "1";
        test[13] = "1";
        test[14] = "2017-05-24-18:47";
        test[15] = "System Administrator";
        list.add(test);
        
        test = new String[16];
        test[0] = "0001384";
        test[1] = "1";
        test[2] = "Xiao Fern";
        test[3] = "Lim";
        test[4] = "Lim Xiao Fern";
        test[5] = "BAH SCCD003 – Cabin Services (T)";
        test[6] = "Cabin Crew (B787) (Christopher Teh (0000848))";
        test[7] = "SC_CC_B787";
        test[8] = "Fixed Term (Fixed Term)";
        test[9] = "2017-05-18-07:00";
        test[10] = "Cabin Crew";
        test[11] = "carolxiaofern@gmail.com";
        test[12] = "0";
        test[13] = "1";
        test[14] = "2017-05-24-18:47";
        test[15] = "System Administrator";
        list.add(test);

        test = new String[16];
        test[0] = "0004606";
        test[1] = "0";
        test[2] = "Norazah";
        test[3] = "Binte Tamrin";
        test[4] = "Norazah Binte Tamrin";
        test[5] = "BAH SCCD003 – Cabin Services (T)";
        test[6] = "Cabin Crew (B787) (Christopher Teh (0000848))";
        test[7] = "SC_CC_B787";
        test[8] = "Fixed Term (Fixed Term)";
        test[9] = "2017-05-18-07:00";
        test[10] = "Cabin Crew";
        test[11] = "norazahtamrin@gmail.com";
        test[12] = "1";
        test[13] = "0";
        test[14] = "2017-05-24-18:47";
        test[15] = "System Administrator";
        list.add(test);
        
        test = new String[16];
        test[0] = "0004607";
        test[1] = "0";
        test[2] = "Chui Lin";
        test[3] = "Wong";
        test[4] = "Wong Chui Lin";
        test[5] = "BAH SCCD003 – Cabin Services (T)";
        test[6] = "Cabin Crew (B787) (Christopher Teh (0000848))";
        test[7] = "SC_CC_B787";
        test[8] = "Fixed Term (Fixed Term)";
        test[9] = "2017-05-18-07:00";
        test[10] = "Cabin Crew";
        test[11] = "chuilin6500@gmail.com";
        test[12] = "0";
        test[13] = "0";
        test[14] = "2017-05-24-18:47";
        test[15] = "System Administrator";
        list.add(test);
        
        ticketController(list);
    }
}