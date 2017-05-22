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


