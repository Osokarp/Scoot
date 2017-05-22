import org.w3c.dom.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.*;
import javax.xml.soap.*;
import java.io.*;
public class SOAPToDocument {
    /**
        * Method used to convert SOAPMessage to Document
        */
    protected static Document toDocument(SOAPMessage soapMsg) 
    throws TransformerConfigurationException, TransformerException, SOAPException, IOException {
        Source src = soapMsg.getSOAPPart().getContent();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMResult result = new DOMResult();
        transformer.transform(src, result);
        return (Document)result.getNode();
    }
}