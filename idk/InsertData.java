import java.sql.*;
import java.util.*;
import java.text.*;
import com.mysql.jdbc.exceptions.*;
public class InsertData{
    protected static void insert (List<String[]> list){
        updateDateCreationAndCreatedBy(list);
        
        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("insert into wsis.workday_report values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            
            //insert into DB
            
            for (int i = 0; i < list.size(); i++){
                String [] str = list.get(i);
                for (int j = 1; j <= str.length; j++){
                    ps.setString(j, str[j-1]);
                }
                try{
                    ps.executeUpdate();
                }catch(SQLException e){
                    String employee_ID = str[0];
                    String hire_date = str[9];
                    String[] template = DocumentToString.setTemplate();

                    PreparedStatement psException = connection.prepareStatement("SELECT * FROM wsis.workday_report where Employee_ID = ? AND Hire_Date = ?;");
                    psException.setString(1, employee_ID);
                    psException.setString(2, hire_date);
                    ResultSet rs = psException.executeQuery();
                    String[] oldStr = new String[template.length];
                    while (rs.next()){
                        for (int j = 1; j <= oldStr.length; j++){
                            oldStr[j-1] = rs.getString(j);
                        }
                    }
                    
                    HashMap<String, String> map = new HashMap<>();
                    
                    for (int j = 0; j < oldStr.length; j++){
                        if (oldStr[j] == null){
                            //k is field name to query in DB
                            //v is value from new String[] from workday
                            map.put(template[j], str[j]);
                        }
                    }

                    Set<String> set = map.keySet();
                    Iterator<String> iter = set.iterator();
                    while (iter.hasNext()){
                        String key = iter.next();
                        String v = key.substring(3);
                        psException = connection.prepareStatement("UPDATE workday_report SET " + v + " = ? WHERE Employee_ID = ? AND Hire_Date = ?;");
                        
                        
                        psException.setString(1, map.get(key));

                        psException.setString(2, employee_ID);

                        psException.setString(3, hire_date);
                        
                        psException.executeUpdate();
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    protected static void updateDateCreationAndCreatedBy (List <String[]> list){
        //date = current date time
        //created by "System Administrator"
        java.util.Date dNow = new java.util.Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'-'HH:mm");
        for (String[] str: list){
            str[str.length-2] = fmt.format(dNow);
            str[str.length-1] = "System Administrator";
        }
    }
}