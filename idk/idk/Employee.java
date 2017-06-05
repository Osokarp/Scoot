/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PrakosoNB
 */
public class Employee {
    private String employeeId;
    private String rehire;
    private String firstName;
    private String lastName;
    private String fullLegalName;
    private String department;
    private String departmentBranch;
    private String supervisoryOrganization;
    private String employmentType;
    private String hireDate;
    private String position;
    private String homeEmail;
    private String ssqeValidation;
    private String itopsValidation;
    
    public Employee(String employeeId, String rehire, String firstName, String lastName, String fullLegalName
    , String department, String departmentBranch, String supervisoryOrganization, String employmentType, String hireDate
    , String position, String homeEmail, String ssqeValidation, String itopsValidation){
        this.employeeId = employeeId;
        this.rehire = rehire;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullLegalName = fullLegalName;
        this.department = department;
        this.departmentBranch = departmentBranch;
        this.supervisoryOrganization = supervisoryOrganization;
        this.employmentType = employmentType;
        this.hireDate = hireDate;
        this.position = position;
        this.homeEmail = homeEmail;
        this.ssqeValidation = ssqeValidation;
        this.itopsValidation = itopsValidation;
        
        
    }
    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the rehire
     */
    public String getRehire() {
        return rehire;
    }

    /**
     * @param rehire the rehire to set
     */
    public void setRehire(String rehire) {
        this.rehire = rehire;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the fullLegalName
     */
    public String getFullLegalName() {
        return fullLegalName;
    }

    /**
     * @param fullLegalName the fullLegalName to set
     */
    public void setFullLegalName(String fullLegalName) {
        this.fullLegalName = fullLegalName;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the departmentBranch
     */
    public String getDepartmentBranch() {
        return departmentBranch;
    }

    /**
     * @param departmentBranch the departmentBranch to set
     */
    public void setDepartmentBranch(String departmentBranch) {
        this.departmentBranch = departmentBranch;
    }

    /**
     * @return the supervisoryOrganization
     */
    public String getSupervisoryOrganization() {
        return supervisoryOrganization;
    }

    /**
     * @param supervisoryOrganization the supervisoryOrganization to set
     */
    public void setSupervisoryOrganization(String supervisoryOrganization) {
        this.supervisoryOrganization = supervisoryOrganization;
    }

    /**
     * @return the employmentType
     */
    public String getEmploymentType() {
        return employmentType;
    }

    /**
     * @param employmentType the employmentType to set
     */
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    /**
     * @return the hireDate
     */
    public String getHireDate() {
        return hireDate;
    }

    /**
     * @param hireDate the hireDate to set
     */
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return the homeEmail
     */
    public String getHomeEmail() {
        return homeEmail;
    }

    /**
     * @param homeEmail the homeEmail to set
     */
    public void setHomeEmail(String homeEmail) {
        this.homeEmail = homeEmail;
    }

    /**
     * @return the ssqeValidation
     */
    public String getSsqeValidation() {
        return ssqeValidation;
    }

    /**
     * @param ssqeValidation the ssqeValidation to set
     */
    public void setSsqeValidation(String ssqeValidation) {
        this.ssqeValidation = ssqeValidation;
    }

    /**
     * @return the itopsValidation
     */
    public String getItopsValidation() {
        return itopsValidation;
    }

    /**
     * @param itopsValidation the itopsValidation to set
     */
    public void setItopsValidation(String itopsValidation) {
        this.itopsValidation = itopsValidation;
    }
    
}
