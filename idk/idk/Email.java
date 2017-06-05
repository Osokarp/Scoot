<<<<<<< HEAD
import com.sun.mail.smtp.SMTPTransport;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.logging.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
public class Email{
    private static final String SMTP_HOST_NAME = "smtp.live.com";
    private static final String SMTP_AUTH_USER = "goucaer@hotmail.com";
    private static final String SMTP_AUTH_PWD  = "Hairy!@#";

    protected static void sendEmail (List<String[]> list, String recipientEmail, int descNumber){
        String subject = "";
        try{
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.user", SMTP_AUTH_USER);
            props.put("mail.smtp.password", SMTP_AUTH_PWD);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "false");

            Session mailSession = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
                }
            }); 
            
            subject = subjectTemplate(descNumber);
            String msg = messageTemplate(descNumber);
            String file = listToCSV(list);
            
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachPart = new MimeBodyPart();
            attachPart.attachFile(file);
            multipart.addBodyPart(attachPart);

            message.setContent(multipart);
            message.setFrom(new InternetAddress(SMTP_AUTH_USER));
            message.setSubject(subject);
            message.addRecipient(Message.RecipientType.TO,
            new InternetAddress(recipientEmail));

            transport.connect();
            transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
            transport.close();
        }catch (SendFailedException e){
            Logs.writeTo("", "email", "for " + subject, "", "failure", e.toString());
        } catch (MessagingException e) {
            Logger.getLogger(EmailTest.class.getName()).log(Level.SEVERE, null, e);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
    protected static String messageTemplate(int descNumber){
        String description = "";
        switch (descNumber){
        case 1: description = "Automatic ticket creation failed for the attached list of new/re-hire(s) due to missing fields. Please fill them up ASAP as they are required for creation of email/security accesses. \n Thank you. \n This is an auto-generated email. Please do not reply.";
            break;
            
        case 2: description = "Automatic ticket creation failed after >= 30 attempts for the attached list of new/re-hire(s). Please manually create them and update (WEBSITE NAME).\n Thank you. \n This is an auto-generated email. Please do not reply.";
            break;
        }
        
        return description;
        
    }
    
    protected static String subjectTemplate (int descNumber){
        String subject = "";
        
        switch (descNumber){
        case 1: subject = "Automatic reply: Automatic ticket creation failed: Missing fields";
        break;
        
        case 2: subject = "Automatic reply: Automatic ticket creation failed";
        break;
        }
        
        return subject;
    }
    
    protected static String listToCSV (List<String[]> list){
        String fileName = "C:\\Users\\Sin Jean Min\\Documents\\Internships\\Scoot 2017\\WSIS\\WSDLSOAP\\idk\\Employee_Data.csv";
        try(PrintStream ps = new PrintStream (new FileOutputStream(fileName),false)){
            ps.print("Employee_ID,Rehire,First_Name,Last_Name,Full_Legal_Name,Department,Department_Branch,Supervisory_Organization,Employment_Type,Hire_Date,Position,Home_Email,SSQE_Validation,ITOps_Validation,Date_Created,Updated_By");

            if (!list.isEmpty() && list.get(0).length == 17){
                ps.print(",Ticket_To_Create");
            }
            
            ps.println();
            
            for (String[] str : list){
                for (int i = 0; i < str.length; i++){
                    ps.print("\t" + str[i] + ",");
                }
                ps.println();
            }
            
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        return fileName;
    }
}


=======
import com.sun.mail.smtp.SMTPTransport;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.util.*;
import java.util.logging.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
public class Email{
    private static final String SMTP_HOST_NAME = "smtp.office365.com";
    private static final String SMTP_AUTH_USER = "jeanmin_sin@scoot-tigerair.com";
    private static final String SMTP_AUTH_PWD  = "SMUScoot123";
    
    public static void main (String args[]){
        sendEmail("test header", "test body", null, "jeanmin.sin@hotmail.com");
    }
    
    public static void sendEmail (String subject, String msg, String file, String recipientEmail){
        try{
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.user", SMTP_AUTH_USER);
            props.put("mail.smtp.password", SMTP_AUTH_PWD);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "false");

            Session mailSession = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
                }
            }); 
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            multipart.addBodyPart(messageBodyPart);
            
            if (file != null){
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(file);
                multipart.addBodyPart(attachPart);
            }

            message.setContent(multipart);
            message.setFrom(new InternetAddress(SMTP_AUTH_USER));
            message.setSubject(subject);
            message.addRecipient(Message.RecipientType.TO,
            new InternetAddress(recipientEmail));

            transport.connect();
            transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (MessagingException e) {
            Logger.getLogger(EmailTest.class.getName()).log(Level.SEVERE, null, e);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}


>>>>>>> ba05079976e5b3aa82b2bcf6c72b8f61ea429140
