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
 //java -cp commons-httpclient-3.1.jar;commons-codec-1.10.jar;commons-logging-1.2.jar; PostAction/
 
 public class PostAction {
 	public static void main(String[] args) throws IOException {
 		PostAction restAction = new PostAction();
 		restAction.postRequest();
 	}
 
 	private void postRequest() throws HttpException, IOException {
 		// This must be valid json string with valid fields and values from table
 		//String postData ="{\"sysparm_quantity\":\"1\",\"variables\":{\"Network_folder_to_be_granted\":\"hello\",\"first_approver\":\"2a0fb94ddb9ca2001c293fa0cf9619ff\"}}";
 		String postData ="{\"sysparm_quantity\":\"1\",\"variables\":{\"Network_folder_to_be_granted\":\"testest\",\"first_approver\":\"6a33af08dbd4a200bddfda9ebf96199a\"}}";
        
 		HttpClient client = new HttpClient();
 		client.getParams().setAuthenticationPreemptive(true);
 		Credentials creds = new UsernamePasswordCredentials("jeanmin_sin@scoot-tigerair.com", "SMUScoot123");
 		client.getState().setCredentials(AuthScope.ANY, creds);
 
 		//PostMethod method = new PostMethod("https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/items/52b5f1270fe11600198288cce1050e89/add_to_cart");
        
        PostMethod method = new PostMethod(" https://scoottigerairdev.service-now.com/api/sn_sc/servicecatalog/items/7bc99a9ddb367a00c4b95040cf9619f0/add_to_cart");
       
 		method.addRequestHeader("Accept", "application/json");
 		method.addRequestHeader("Content-Type", "application/json");
 		method.setRequestEntity(new ByteArrayRequestEntity(postData.getBytes()));
 
 		int status = client.executeMethod(method);
 		System.out.println("Status:" + status);
 		BufferedReader rd = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
 		String line = "";
 		while ((line = rd.readLine()) != null) {
 			System.out.println(line);
  		}
 	}
 }