import java.util.*;
import java.text.*;
import javax.xml.soap.*;
import javax.xml.namespace.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
public class CreateSOAPRequest{
    protected static SOAPMessage createRequest() throws Exception
    {
        Date dNow = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        
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
        
        SOAPElement authentication = executeReport.addChildElement("Authentication", "jean");

        soapMessage.saveChanges();
        
        // Check the input
        /* System.out.println("Request SOAP Message = ");
        soapMessage.writeTo(System.out);
        System.out.println(); */
        return soapMessage;
    }
    
    /**
        * Method used to print the SOAP Response
        */
    protected static void printSOAPResponse(SOAPMessage soapResponse) throws Exception
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        System.out.println("\nResponse SOAP Message = ");
        StreamResult result = new StreamResult(System.out);
        transformer.transform(sourceContent, result);
    }
    
}