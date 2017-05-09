import java.sql.*;
import org.gjt.mm.mysql.Driver;
public class InsertData{
    public static void main (String[]args){
        try{
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement("insert into workday_report values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            String [] array = new String[15];
            for (int i = 0; i < array.length; i++){
                array[i] = "apple";
            }
            for (int j = 1; j <= array.length; j++){
                ps.setString(j, array[j-1]);
            }
        }catch(SQLException e){
           
        }
    }
}