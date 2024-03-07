/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universitydb;

/**
 *
 * @author Abdelrahman
 */
public class Student {
    private int studentID;
    private String fName;
    private String lName;
    private String DOB;
    private String faculty;
    private String major;
    private String email;
    private String joinedDate;
    
    // Constructors
    public Student(){};
    public Student(int studentID, String fName, String lName, String DOB, String faculty, String major, String email, String joinedDate) {
        this.studentID = studentID;
        this.fName = fName;
        this.lName = lName;
        this.DOB = DOB;
        this.faculty = faculty;
        this.major = major;
        this.email = email;
        this.joinedDate = joinedDate;
    }
    
    
   // Update the getter and setter names for fName and lName

// SETTERS
public void setStudentID(int studentID) {
    this.studentID = studentID;
}

public void setFName(String fName) { // Update this line
    this.fName = fName;
}

public void setLName(String lName) { // Update this line
    this.lName = lName;
}

public void setDOB(String DOB) {
    this.DOB = DOB;
}

public void setFaculty(String faculty) {
    this.faculty = faculty;
}

public void setMajor(String major) {
    this.major = major;
}

public void setEmail(String email) {
    this.email = email;
}

public void setJoinedDate(String joinedDate) {
    this.joinedDate = joinedDate;
}

// GETTERS
public int getStudentID() {
    return studentID;
}

public String getFName() { // Update this line
    return fName;
}

public String getLName() { // Update this line
    return lName;
}

public String getDOB() {
    return DOB;
}

public String getFaculty() {
    return faculty;
}

public String getMajor() {
    return major;
}

public String getEmail() {
    return email;
}

public String getJoinedDate() {
    return joinedDate;
}

    
}
