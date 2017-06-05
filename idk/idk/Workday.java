import org.w3c.dom.*;
import com.mysql.jdbc.exceptions.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import javax.xml.xpath.*;
import javax.xml.soap.*;
import javax.xml.namespace.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

/* This class does the following:
1. Retrieve SOAPmessage of Workday Report
2. Converts SOAPmessage to Document
3. Converts Document to List <String[]> format, where every String[] is one employee
4. Insert the List<String[]> into database */

public class Workday {
    private static java.util.Date dNow = new java.util.Date();
    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    private static TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private static String[] template;
    
    protected static List<String[]> workDayController(){
        List<String[]> stringArray = null;
        try{
            //Create Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            //Send SOAP Message to SOAP Server
            String url = "https://wd3-impl-services1.workday.com/ccx/service/customreport2/flyscoot/0000952/Jean_New_Hires?wsdl";
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(), url);
            
            //printSOAPResponse(soapResponse);
            
            //SOAPMessage to Document
            Document document = toDocument(soapResponse);
            
            //Document to ArrayList of String[]
            stringArray = toStringArray(document);
            
            //Insert ArrayList of String[] into WSIS
            insertIntoDatabase(stringArray);

            soapConnection.close();
           
        }catch (Exception e){
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
        return stringArray;
    }
    
    protected static SOAPMessage createSOAPRequest() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        
        envelope.addNamespaceDeclaration("jean", "urn:com.workday.report/Jean_New_Hires");
        
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");

        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        
        // SOAP Header     
        SOAPHeader header = envelope.getHeader();
        
        SOAPHeaderElement  security = header.addHeaderElement(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security","wsse"));
        
        security.addAttribute(envelope.createName("soapenv:mustUnderstand"), "1");
        
        security.addAttribute(envelope.createName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
        
        SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
        
        usernameToken.addAttribute(envelope.createName("wsu:Id"), "UsernameToken-FB0237168C71B6EC9714937787922901");
        
        SOAPElement username = usernameToken.addChildElement("Username","wsse");
        
        username.setTextContent("int_tcs_user@flyscoot");
        
        SOAPElement password = usernameToken.addChildElement("Password", "wsse");
        
        password.addAttribute(envelope.createName("Type"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        
        password.setTextContent("M2B2Qs32hmUxzaxa!");
        
        SOAPElement nonce = usernameToken.addChildElement("Nonce","wsse");
        
        nonce.addAttribute(envelope.createName("EncodingType"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
        
        nonce.setTextContent("cR1lTLrY0j2G5kcrAzIEpg==");
        
        SOAPElement created = usernameToken.addChildElement("Created","wsu");
        
        created.setTextContent("2017-04-28T03:08:57.415Z");
        
        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        
        SOAPElement executeReport = soapBody.addChildElement("Execute_Report", "jean");
        
        SOAPElement reportParameters = executeReport.addChildElement("Report_Parameters", "jean");
        
        SOAPElement dateCreated = reportParameters.addChildElement("Prompt_Created_Date_From", "jean");
        
        dateCreated.setTextContent(fmt.format(dNow));
        //dateCreated.setTextContent("2017-05-18");
        
        SOAPElement authentication = executeReport.addChildElement("Authentication", "jean");

        soapMessage.saveChanges();
        
        // Check the input
        /* System.out.println("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println(); */
        
        return soapMessage;
    }
    
    //Method used to print the SOAP Response to check
    protected static void printSOAPResponse(SOAPMessage soapResponse) throws Exception
    {
        //TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.println("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }
    
    //Method used to convert SOAPMessage to Document
    protected static Document toDocument(SOAPMessage soapMsg) 
    throws TransformerConfigurationException, TransformerException, SOAPException, IOException {
        Source src = soapMsg.getSOAPPart().getContent();
        //TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMResult result = new DOMResult();
        transformer.transform(src, result);
        return (Document)result.getNode();
    }
    
    //Method used to set the template needed in DocumentToString.
    //Format must tally with XML nodes that we are looking to extract (except index 14, 15)
    //Helps to determine which fields are missing
    //DO NOT EDIT or remove wd: unless you are pulling out more info from the XML that's not reflected in the existing template. The rest of the application is based on this format.
    //setTemplate(), toStringArray() and insertIntoDatabase() all depends on the SAME template. Change 1, change all.
    protected static String[] setTemplate(){
        template = new String [16];
        template[0] = "wd:Employee_ID";
        template[1] = "wd:Rehire";
        template[2] = "wd:First_Name";
        template[3] = "wd:Last_Name";
        template[4] = "wd:Full_Legal_Name";
        template[5] = "wd:Department";
        template[6] = "wd:Department_Branch";
        template[7] = "wd:Supervisory_Organization";
        template[8] = "wd:Employment_Type";
        template[9] = "wd:Hire_Date";
        template[10] = "wd:Position";
        template[11] = "wd:Home_Email";
        template[12] = "wd:SSQE_Validation";
        template[13] = "wd:ITOps_Validation";
        template[14] = "wd:Date_Created";
        template[15] = "wd:Updated_By";
        return template;
    }
    
    //Method used to parse the Document (in its XML format) into String[] that we need
    //Index 14 and 15 will be updated later, so it will always be null
    //DO NOT EDIT or remove wd: unless you are pulling out more info from the XML that's not reflected in the existing template. The rest of the application is based on this format.
    //setTemplate(), toStringArray() and insertIntoDatabase() all depends on the SAME template. Change 1, change all.
    protected static List<String[]> toStringArray(Document xmlDocument){
        DocumentBuilderFactory builderFactory =
        DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        List<String[]> workdayList = null;     

        try {
            workdayList = new ArrayList<>();
            
            builder = builderFactory.newDocumentBuilder();
            
            //creating an XPath object
            XPath xPath =  XPathFactory.newInstance().newXPath();
            
            //Get all new hires
            NodeList nodeList = xmlDocument.getElementsByTagName("wd:Report_Entry");
            
            //Check which fields are missing
            for (int i = 0; i < nodeList.getLength(); i++){
                //result is the end result of field names + null fields if any
                setTemplate();
                String[] result = new String [template.length];
                NodeList nodeListFields = nodeList.item(i).getChildNodes();
                
                //Add fields from Workday into ArrayList
                ArrayList<String> extractedFields = new ArrayList<>();
                for (int j = 0; j < nodeListFields.getLength(); j++){
                    String field = nodeListFields.item(j).getNodeName();
                    extractedFields.add(field);
                }
                
                //Compare extractedFields against template to see what is missing
                for (int j = 0; j < extractedFields.size(); j++){
                    for (int k = 0; k < template.length; k++){
                        if (extractedFields.get(j).equals(template[k])){
                            result[k] = template[k];
                            break;
                        }
                    }
                }
                workdayList.add(result);
            }
            
            for (int i = 0; i < workdayList.size(); i++){
                String[] result = workdayList.get(i);
                NodeList nodeListFields = nodeList.item(i).getChildNodes();
                
                for (int j = 0; j < nodeListFields.getLength(); j++){
                    org.w3c.dom.Node node = nodeListFields.item(j);
                    String field = node.getNodeName();
                    org.w3c.dom.Node firstChild = node.getFirstChild(); //will always have first child
                    String fieldValue = null;
                    boolean firstChildHasChild = firstChild.hasChildNodes();
                    if (firstChildHasChild){
                        if (field.equals("wd:Supervisory_Organization")){
                            org.w3c.dom.Node supervisoryNode = firstChild.getFirstChild();
                            fieldValue = supervisoryNode.getNodeValue();
                        }else{
                            NamedNodeMap map = node.getAttributes();
                            for (int k = 0; k < map.getLength(); k++){
                                org.w3c.dom.Node mapNode = map.item(k);
                                fieldValue = mapNode.getNodeValue();  
                            }                    
                        }
                    }else{
                        fieldValue = firstChild.getNodeValue();
                    }
                    
                    for (int k = 0; k < result.length; k++){
                        if (result[k] != null && result[k].equals(field)){
                            result[k] = fieldValue;
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  
        }
        
        //Loop to print and check if all fields are correct
        /* for (String[] result : workdayList){
            for (int i = 0; i < result.length; i++){
                System.out.println(result[i]);
            }
            System.out.println("~~~~~~~~~SEPARATE ENTRY~~~~~~~~~");
        } */
        
        return workdayList;
    }
    
    //Method used to update Index 14 (Date_Created) & 15 (Updated_By)
    //Date_Created = Current Date Time
    //Created = "System Administrator"
    protected static void updateDateCreationAndCreatedBy (List <String[]> list){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm");
        for (String[] str: list){
            str[str.length-2] = fmt.format(dNow);
            str[str.length-1] = "System Administrator";
        }
    }
    
    //Method INSERT the ArrayList of String[] of employees into the database
    //If employee detail already exists in database (Duplicate Primary Key exception), this method will UPDATE instead
    //DO NOT EDIT or remove anything unless you are pulling out more info from the XML that's not reflected in the existing template. The rest of the application is based on this format.
    //setTemplate(), toStringArray() and insertIntoDatabase() all depends on the SAME template. Change 1, change all.
    protected static void insertIntoDatabase(List<String[]> list){
        updateDateCreationAndCreatedBy(list);
        
        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("insert into wsis.workday_report values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            
            //insert into DB
            
            for (int i = 0; i < list.size(); i++){
                String [] str = list.get(i);
                for (int j = 1; j <= str.length; j++){
                    ps.setString(j, str[j-1]);
                }
                try{
                    ps.executeUpdate();
                }catch(SQLException e){
                    String employee_ID = str[0];
                    String hire_date = str[9];
                    String[] template = setTemplate();

                    PreparedStatement psException = connection.prepareStatement("SELECT * FROM wsis.workday_report where Employee_ID = ? AND Hire_Date = ?;");
                    psException.setString(1, employee_ID);
                    psException.setString(2, hire_date);
                    ResultSet rs = psException.executeQuery();
                    String[] oldStr = new String[template.length];
                    while (rs.next()){
                        for (int j = 1; j <= oldStr.length; j++){
                            oldStr[j-1] = rs.getString(j);
                        }
                    }
                    
                    HashMap<String, String> map = new HashMap<>();
                    
                    for (int j = 0; j < oldStr.length; j++){
                        if (oldStr[j] == null){
                            //k is field name to query in DB
                            //v is value from new String[] from workday
                            map.put(template[j], str[j]);
                        }
                    }

                    Set<String> set = map.keySet();
                    Iterator<String> iter = set.iterator();
                    while (iter.hasNext()){
                        String key = iter.next();
                        String v = key.substring(3);
                        psException = connection.prepareStatement("UPDATE workday_report SET " + v + " = ? WHERE Employee_ID = ? AND Hire_Date = ?;");
                        
                        
                        psException.setString(1, map.get(key));

                        psException.setString(2, employee_ID);

                        psException.setString(3, hire_date);
                        
                        psException.executeUpdate();
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}