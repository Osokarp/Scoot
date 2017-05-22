import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;
import javax.xml.xpath.*;
public class DocumentToString{
    protected static String[] template;
    
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
    
    protected static List<String[]> toString(Document xmlDocument){
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
                    Node node = nodeListFields.item(j);
                    String field = node.getNodeName();
                    Node firstChild = node.getFirstChild(); //will always have first child
                    String fieldValue = null;
                    boolean firstChildHasChild = firstChild.hasChildNodes();
                    if (firstChildHasChild){
                        if (field.equals("wd:Supervisory_Organization")){
                            Node supervisoryNode = firstChild.getFirstChild();
                            fieldValue = supervisoryNode.getNodeValue();
                        }else{
                            NamedNodeMap map = node.getAttributes();
                            for (int k = 0; k < map.getLength(); k++){
                                Node mapNode = map.item(k);
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
        /* for (String[] result : workdayList){
            for (int i = 0; i < result.length; i++){
                System.out.println(result[i]);
            }
            System.out.println("~~~~~~~~~~~~~~~~~~");
        } */
        return workdayList;
    }
}