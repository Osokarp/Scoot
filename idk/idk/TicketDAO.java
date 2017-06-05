
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;
import java.sql.*;
import java.io.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PrakosoNB
 */
public class TicketDAO {
    private ArrayList<Ticket> tickets;
    
    public void createTicket(String id, String hiredate, String type){
        ArrayList<Employee> emailList = new ArrayList();
        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("select creation_attempts, ticket_number, ticket_id from wsis.tickets where employee_id = ? and hire_date = ? and ticket_type = ?");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if(rs.next()){
                int creationAttempts = Integer.parseInt(rs.getString(1));
                String ticketNo = rs.getString(2);
                String ticketId = rs.getString(3);
                if (creationAttempts > 30){
                    //Email
                }else if (creationAttempts <= 30 && (ticketNo == null || ticketId == null)){
                    //Create ticket on servicenow
                    int addTryCount = 0;
                    int checkoutTryCount = 0;
                    boolean addPassed = false;
                    boolean checkoutPassed = false;
                    String postData = "";
                    String url = "";
                    EmployeeDAO employees = new EmployeeDAO();
                    Employee e = employees.retrieveEmployee(id, hiredate);
                    if (e != null){
                        String hireString = "";
                        String rehire = e.getRehire();
                        if (rehire.equals("0")){
                            hireString += "new hire";
                        }else{
                            hireString += "rehire";
                        }
                        while (!addPassed && addTryCount < 5){
                            if (type.equals("ssqe")){
                            postData = "{\"sysparm_quantity\":\"1\",\"variables\":{\"Network_folder_to_be_granted\":\"" + hireString + "\",\"first_approver\":\"6a33af08dbd4a200bddfda9ebf96199a\"}}";
                            url = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/items/7bc99a9ddb367a00c4b95040cf9619f0/add_to_cart";
                            }else if(type.equals("itops")){
                            postData = "{\"sysparm_quantity\":\"1\",\"variables\":{\"Network_folder_to_be_granted\":\"" + hireString + "\",\"first_approver\":\"2a0fb94ddb9ca2001c293fa0cf9619ff\"}}";
                            url = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/items/52b5f1270fe11600198288cce1050e89/add_to_cart";
                            }
                            String[] status = createRequest(postData, url);
                            if (status[0].equals("200")){
                                addPassed = true;
                                addTryCount++;
                            }else{
                                addTryCount++;
                            }
                        }
                        
                        while (!checkoutPassed && addPassed && checkoutTryCount < 5){
                            //Checkout
                            String[] status = createRequest("","https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/cart/checkout");
                            if (status[0].equals("200")){
                                checkoutPassed = true;
                                checkoutTryCount++;
                            }else{
                                checkoutTryCount++;
                            }
                        }
                        
                    }
                    
                }
                    
            }else{
                ps = connection.prepareStatement("insert into wsis.tickets values(?,?,?,?,?,?,?)");
                ps.setString(1, id);
                ps.setString(2, hiredate);
                ps.setString(3, type);
                ps.setString(4, "1");
                ps.setString(5, "false");
                ps.setString(6, null);
                ps.setString(7, null);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    protected static String[] createRequest (String postData, String url) throws IOException, HttpException{
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
}
