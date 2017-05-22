/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PrakosoNB
 */
public class Database {
    public static List<String> getAllDBTables() throws SQLException{
            Connection conn = null;
            PreparedStatement connection = null;
            ResultSet rs = null;
            List<String> tables = new ArrayList<String>();
            try {
                
                conn = ConnectionManager.getConnection();
                connection = conn.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE'");
                rs = connection.executeQuery();
                while (rs.next()){
                    tables.add(rs.getString(3));
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
                connection.setString(1, tablename);
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
    
    public static boolean checkPassword(String username, String passwordHash){
        Connection conn = null;
            PreparedStatement connection = null;
            ResultSet rs = null;
            try {
                
                conn = ConnectionManager.getConnection();
                connection = conn.prepareStatement("SELECT password FROM user where username = ?");
                connection.setString(1,username);
                rs = connection.executeQuery();
                if (rs.next()){
                    if (rs.getString(1).equals(passwordHash)){
                        System.out.println("match");
                        return true;
                    }else{
                        return false;
                    }
                }
                return true;

            } catch (SQLException ex) {
                 System.out.println(ex);
            } finally {
                ConnectionManager.close(conn, connection, rs);
            }
            return false;
    }
    
    public static boolean changePassword(String username, String password) throws UnsupportedEncodingException, SQLException{
        Connection conn = null;
            PreparedStatement connection = null;
            ResultSet rs = null;
            try {
                
                conn = ConnectionManager.getConnection();
                connection = conn.prepareStatement("UPDATE user SET password = ? where username = ?");
                connection.setString(1,Encryption.sha512Encryption(password));
                connection.setString(2,username);
                connection.executeUpdate();
                return true;

            } catch (SQLException ex) {
                 return false;
            } finally {
                ConnectionManager.close(conn, connection, rs);
            }
            
    }
}
