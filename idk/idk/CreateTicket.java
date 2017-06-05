import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
//javac -cp commons-httpclient-3.1.jar;commons-codec-1.10.jar; CreateTicket.java
public class CreateTicket {
    public static void main (String[]args) throws IOException{
        String username = "jeanmin_sin@scoot-tigerair.com";
        String password = "SMUScoot123";
        String postDataITOps= "{\"sysparm_quantity\":\"1\",\"variables\":{\"Network_folder_to_be_granted\":\"hello\",\"first_approver\":\"2a0fb94ddb9ca2001c293fa0cf9619ff\"}}";
        String urlITOps = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/items/52b5f1270fe11600198288cce1050e89/add_to_cart";
        
        String postDataSSQE = "{\"sysparm_quantity\":\"1\",\"variables\":{\"Network_folder_to_be_granted\":\"testest\",\"first_approver\":\"2a0fb94ddb9ca2001c293fa0cf9619ff\"}}";
        String urlSSQE = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/items/7bc99a9ddb367a00c4b95040cf9619f0/add_to_cart";
        
        String urlCheckOut = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/cart/checkout";
        postRequest(postDataITOps, urlITOps, username, password);
        postRequest("", urlCheckOut, username, password);
        postRequest(postDataSSQE, urlSSQE, username, password);
        postRequest("", urlCheckOut, username, password);
    }
    
    protected static String[] postRequest(String postData, String url, String username, String password) throws IOException, HttpException{
        String[] result = new String[3];
        //postData must be a valid json string with valid fields and values from table!
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials creds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(AuthScope.ANY, creds);
        
        PostMethod method = new PostMethod(url);
        
        method.addRequestHeader("Accept", "application/json");
        method.addRequestHeader("Content-Type", "application/json");
        method.setRequestEntity(new ByteArrayRequestEntity(postData.getBytes()));
        
        int status = client.executeMethod(method);
        result[0] = status;
        System.out.println("Status:" + status);
        
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
    
    //validateRequest will try 5 times if the request fails
    //and return boolean value if it fails or pass
    protected static String[] validateRequest(String postData, String url, String username, String password){
        String[] result = postRequest(postData, url, username, password);
        int attemptCount = 0;
        while (!result[0].equals("200") && attemptCount < 5){
            request = postRequest(postData, url, username, password);
            attemptCount++;
        }
        
        String urlCheckOut = "https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/cart/checkout";
        if (result[0].equals("200")){
            if (!url.equals(urlCheckOut)){
                validateRequest("", urlCheckOut, username, password);    
            }
            //update database 
            //the string array 1,2 will be filled
            return true;
        }else{
            //update database
            //the string array 1,2 will be empty
            
            return false;   
        }
    }
    
    protected static void requestController(String postData, String url, String username, String password{
        //if true, try validating add to card
        //if received false, update immediately?
        boolean 
    }
}