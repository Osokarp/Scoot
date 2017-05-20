import javax.xml.soap.*;
import org.w3c.dom.*;
import java.util.*;
//javac -cp mysql-connector-java-5.1.42-bin.jar; SOAPTest.java
public class SOAPTest{
    public static void main (String[]args){
        try{
            //Create Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            //Send SOAP Message to SOAP Server
            String url = "https://wd3-impl-services1.workday.com/ccx/service/customreport2/flyscoot/0000952/Jean_New_Hires?wsdl";
            SOAPMessage soapResponse = soapConnection.call(CreateSOAPRequest.createRequest(), url);
            
            //SOAPMessage to Document
            Document doc = SOAPToDocument.toDocument(soapResponse);
            
            //Document to String
            List<String[]> result = DocumentToString.toString(doc);
            
            InsertData.insert(result);
            
            // String[] test = new String[16];
            // test[0] = "0001371";
            // test[1] = "0";
            // test[2] = "Jeng Shun (Qiu Zhengxun)";
            // test[3] = "Koo";
            // test[4] = "Jeng Shun (Qiu Zhengxun) Koo";
            // test[5] = "Apples";
            // test[6] = "Cadet Pilot (B787) (Edwin Verner Jesudason (0002049) (Inherited))";
            // test[7] = "SC_CP_B787";
            // test[8] = "Cadet Pilot";
            // test[9] = "2017-01-23-08:00";
            // test[10] = "Cadet Pilot";
            // test[11] = "jeng_shun@hotmail.com";
            // test[12] = "0";
            // test[13] = "0";
            
            // List<String[]> result = new ArrayList<>();
            // result.add(test);
            // InsertData.insert(result);
            
            soapConnection.close();
        }catch (Exception e){
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
        }
    }
}