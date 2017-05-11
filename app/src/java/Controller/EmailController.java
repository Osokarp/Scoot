/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import com.sun.mail.smtp.SMTPTransport;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 *
 * @author PrakosoNB
 */
@WebServlet(name = "EmailController", urlPatterns = {"/EmailController"})
public class EmailController extends HttpServlet {
    private static final String SMTP_HOST_NAME = "smtp.live.com";
        private static final String SMTP_AUTH_USER = "goucaer@hotmail.com";
        private static final String SMTP_AUTH_PWD  = "harrow123";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
                /*  String to = "awssadasd@hotmail.com";

      // Sender's email ID needs to be mentioned
      String from = "awssadasd@hotmail.com";

      // Assuming you are sending email from localhost
      String host = "localhost";
      
      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", "smtp.live.com");
      properties.setProperty("mail.smtp.port", "465"); 
      properties.put("mail.smtp.auth", "true");
      properties.put("mail.smtp.user", "goucaer@hotmail.com");
      properties.put("mail.smtp.password", "harrow123");
      //properties.put("mail.smtp.socketFactory.port", "587");  
      properties.put("mail.smtp.starttls.enable", "true");
      //properties.put("mail.smtp.socketFactory.port", "465");
      //properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
      //properties.put("mail.smtp.socketFactory.fallback", "false"); 
      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties,
    new Authenticator() {
        protected PasswordAuthentication  getPasswordAuthentication() {
        return new PasswordAuthentication(
                    "goucaer@hotmail.com", "harrow123");
                }
    });
      session.setDebug(true);
      try {
         SMTPTransport transport =
		(SMTPTransport)session.getTransport("smtp");
         
        //Transport transport = session.getTransport("smtp");  
   InternetAddress addressFrom = new InternetAddress(from);  

   MimeMessage message = new MimeMessage(session);  
   message.setSender(addressFrom);  
   message.setSubject("hello");  
   message.setContent("test", "text/plain");  
   message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  

   transport.connect("smtp.live.com","goucaer@hotmail.com","harrow123");  
   Transport.send(message);  
   transport.close();
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }*/
                Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.user", "goucaer@hotmail.com");
      props.put("mail.smtp.password", "harrow123");
      props.put("mail.smtp.starttls.enable", "true");

            Authenticator auth = new SMTPAuthenticator();
            Session mailSession = Session.getDefaultInstance(props, auth);
            // uncomment for debugging infos to stdout
            // mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);

            Multipart multipart = new MimeMultipart("alternative");

            BodyPart part1 = new MimeBodyPart();
            part1.setText("Checking to see what box this mail goes in ?");

            BodyPart part2 = new MimeBodyPart();
            part2.setContent("Checking to see what box this mail goes in ?", "text/html");

            multipart.addBodyPart(part1);
            multipart.addBodyPart(part2);

            message.setContent(multipart);
            message.setFrom(new InternetAddress("fake@hotmail.com"));
            message.setSubject("Can you see this mail ?");
            message.addRecipient(Message.RecipientType.TO,
                 new InternetAddress("awssadasd@hotmail.com"));

            transport.connect();
            transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (MessagingException ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
private class SMTPAuthenticator extends javax.mail.Authenticator {
            public PasswordAuthentication getPasswordAuthentication() {
               String username = SMTP_AUTH_USER;
               String password = SMTP_AUTH_PWD;
               return new PasswordAuthentication(username, password);
            }
        }
}


