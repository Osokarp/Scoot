import java.util.*;
import java.sql.*;
import java.sql.SQLException;

public class FilterData {
    public static List<String> getAllDBTables() throws SQLException{
        Connection conn = null;
        PreparedStatement connection = null;
        ResultSet rs = null;
        List<String> tables = new ArrayList<String>();
        try {
            conn = ConnectionManager.getConnection();
            connection = conn.prepareStatement("");
            rs = connection.executeQuery();
            while (rs.next()){
                tables.add(rs.getString(1)); 
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            ConnectionManager.close(conn, connection, rs);
        }
        return tables;
    }
    
    public static void addColumns(List<String> settings) throws SQLException{
        Connection conn = null;
        PreparedStatement connection = null;
        ResultSet rs = null;
        String tablename = settings.get(0);
        String fieldname = settings.get(1);
        String nullnotnull = settings.get(2);
        String datatype = settings.get(3);
        String size = settings.get(4);
        String decimal = settings.get(5);
        
        String columnDefinition = "";
        if (size != null && decimal != null){
            columnDefinition += datatype + "(" + size + ", " + decimal + ")";
        }else if (size != null && decimal == null){
            columnDefinition += datatype + "(" + size + ")";
        }else if (size == null && decimal == null){
            columnDefinition += datatype;
        }
        try {  
            conn = ConnectionManager.getConnection();
            connection = conn.prepareStatement("ALTER TABLE ? ADD ? ? ?;");
            connection.setString(1, tablename); // setString sets the value of a question mark in the statement (xth ?, value)
            connection.setString(2, fieldname);
            connection.setString(3, columnDefinition);
            if (nullnotnull != null){
                connection.setString(4, nullnotnull);
            }else{
                connection.setString(4, "");
            }
            connection.executeUpdate();

        } catch (SQLException ex) {
            throw ex;
        } finally {
            ConnectionManager.close(conn, connection, rs);
        }
    }
}