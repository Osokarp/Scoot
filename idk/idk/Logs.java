import java.io.*;
import java.text.ParseException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.charset.Charset;

public class Logs {
    //create a .txt file by date, dont have to create if nothing to be logged
    //if file is created, append
    //file name should be date format DDMMYYYY
    
    //to print -> create a method that takes in an arraylist of String[], as well as int
    //the string[] is the hire details passed in. so that Logs can extract the Employee ID and print to the txt file
    //the int is for switchcase descriptions to insert into description . e.g. 1 == missing fields, 2 == failed request, 3 == successful request
    //do switchcase 1 2 3 according to above
    //print as per format -> datetime:empID:http status code:description (taken from switchcase description above) (successful, or missing fields, failed request etc)etc)
	/* public static void writeTo(String[] str, int descNumber, int status, String ticketType){
		FileOutputStream fop = null;
		File file;

		try {  
			final String loc = "C:\\Users\\Sin Jean Min\\Documents\\Internships\\Scoot 2017\\WSIS\\WSDLSOAP\\idk\\";
			final String ext = ".txt";
			
			// This block configure the logger with handler and formatter
			Date today = new Date();
			String todayString = today.getDate() + "-" + (today.getMonth()+1) + "-" + (1900 + today.getYear()) + " " + today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds(); 
			
			String description = "";
			switch (descNumber) {
				case 1: description = "Missing fields, emailed HR";
				break;
				case 2: description = "Failed request for " + ticketType + " ticket";
				break;
				case 3: description = "Successful request for " + ticketType + " ticket";
				break;
                case 4: description = "Attempted ticket creation on ServiceNow for >= 30 times, emailed System Administrator to manually process request for " + ticketType + " ticket";
                break;
                case 5: description = "";
                break;
			}
			
			file = new File(loc + today.getDate() + "-" + today.getMonth() + "-" + (1900 + today.getYear()) + ext);
			fop = new FileOutputStream(file, true);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			fop.write((todayString + "|" + str[0] + "|" + status + "|" + description + "\r\n").getBytes(Charset.forName("UTF-8")));
			// the following statement is used to log any messages  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} 
	} */
    
    protected static void writeTo(String empID, String action, String actionDescription, String ticketType, String status, String statusDescription){
        FileOutputStream fop = null;
		File file;

		try {  
			final String loc = "C:\\Users\\Sin Jean Min\\Documents\\Internships\\Scoot 2017\\WSIS\\WSDLSOAP\\idk\\";
			final String ext = ".txt";
			
			// This block configure the logger with handler and formatter
            
			Date today = new Date();
			
            String todayString = today.getDate() + "-" + (today.getMonth()+1) + "-" + (1900 + today.getYear()) + " " + today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();        
			
			file = new File(loc + today.getDate() + "-" + today.getMonth() + "-" + (1900 + today.getYear()) + ext);
			fop = new FileOutputStream(file, true);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			/* fop.write((todayString + "|" + str[0] + "||" + description + "\r\n").getBytes(Charset.forName("UTF-8"))); */
            
            fop.write((todayString + "|" + empID + "|" + action + "|" + actionDescription + "|" + ticketType + "|" + status + "|" + statusDescription + "\r\n").getBytes(Charset.forName("UTF-8")));
			// the following statement is used to log any messages  

		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		} 
    }
	
}