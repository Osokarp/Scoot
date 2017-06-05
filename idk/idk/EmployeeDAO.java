import java.sql.*;
import java.util.*;
import java.text.*;
import com.mysql.jdbc.exceptions.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PrakosoNB
 */
public class EmployeeDAO {

private List<Employee> employees;
    public void addEmployees (){
        
        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("insert into wsis.employees values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                java.util.Date dNow = new java.util.Date();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            //insert into DB
            for (int i = 0; i < employees.size(); i++){
                Employee e = employees.get(i);
                ps.setString(1, e.getEmployeeId());
                ps.setString(2, e.getRehire());
                ps.setString(3, e.getFirstName());
                ps.setString(4, e.getLastName());
                ps.setString(5, e.getFullLegalName());
                ps.setString(6, e.getDepartment());
                ps.setString(7, e.getDepartmentBranch());
                ps.setString(8, e.getSupervisoryOrganization());
                ps.setString(9, e.getEmploymentType());
                ps.setString(10, e.getHireDate());
                ps.setString(11, e.getPosition());
                ps.setString(12, e.getHomeEmail());
                ps.setString(13, e.getSsqeValidation());
                ps.setString(14, e.getItopsValidation());
                ps.setString(15, fmt.format(dNow));
                ps.setString(16, "System Administrator");
                ps.executeUpdate();
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Employee retrieveEmployee (String id, String hiredate){
        
        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("select * from wsis.employees where Employee_ID = ? and Hire_Date = ?");
            ps.setString(1, id);
            ps.setString(2, hiredate);
            ResultSet rs = null;
            rs = ps.executeQuery();
            if(rs.next()){
                return new Employee(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public void updateEmployees (String id, String hiredate){

        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("update wsis.employees set Employee_ID = ? and Rehire = ? and First_Name = ? and Last_Name = ? and Full_Legal_Name = ? "
                                                                + "and Department = ? and Department_Branch = ? and Supervisory_Organization = ? and Employment_Type = ?"
                                                                    + "and Hire_Date = ? and Position = ? and Home_Email = ? and SSQE_Validation = ? and ITOps_Validation = ? where Employee_ID = ? and Hire_Date = ?");
            for (int i = 0; i < employees.size(); i++){
                Employee e = employees.get(i);
                ps.setString(1, e.getEmployeeId());
                ps.setString(2, e.getRehire());
                ps.setString(3, e.getFirstName());
                ps.setString(4, e.getLastName());
                ps.setString(5, e.getFullLegalName());
                ps.setString(6, e.getDepartment());
                ps.setString(7, e.getDepartmentBranch());
                ps.setString(8, e.getSupervisoryOrganization());
                ps.setString(9, e.getEmploymentType());
                ps.setString(10, e.getHireDate());
                ps.setString(11, e.getPosition());
                ps.setString(12, e.getHomeEmail());
                ps.setString(13, e.getSsqeValidation());
                ps.setString(14, e.getItopsValidation());
                ps.setString(15, id);
                ps.setString(16, hiredate);
                ps.executeUpdate();
            }

        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ArrayList<Employee> validate(){
        ArrayList<Employee> emailList = new ArrayList();
        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("select * from wsis.employees where SSQE_Validation = 0 or ITOps_Validation = 0");
            ResultSet rs = null;
            rs = ps.executeQuery();
            while(rs.next()){
                emailList.add(new Employee(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14)));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return emailList;
        
    }
    
    public ArrayList<Employee> validateSSQE(){
        ArrayList<Employee> emailList = new ArrayList();
        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("select * from wsis.employees where SSQE_Validation = 1");
            ResultSet rs = null;
            rs = ps.executeQuery();
            while(rs.next()){
                emailList.add(new Employee(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14)));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return emailList;
        
    }
    
    public ArrayList<Employee> validateITOps(){
        ArrayList<Employee> emailList = new ArrayList();
        try(Connection connection = ConnectionManager.getConnection()){
            PreparedStatement ps = connection.prepareStatement("select * from wsis.employees where ITOps_Validation = 1");
            ResultSet rs = null;
            rs = ps.executeQuery();
            while(rs.next()){
                emailList.add(new Employee(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14)));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        return emailList;
        
    }
    
    
}
