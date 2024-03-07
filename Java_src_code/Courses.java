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
public class Courses {
    private int studentID;
    private int courseID;
    private int semesterID;
    private float grade;
    private int mappedGrade;
    private String gradeLetter;

    public Courses (){};
    public Courses(int studentID, int courseID, int semesterID, float grade, int mappedGrade, String gradeLetter) {
        this.studentID = studentID;
        this.courseID = courseID;
        this.semesterID = semesterID;
        this.grade = grade;
        this.mappedGrade = mappedGrade;
        this.gradeLetter = gradeLetter;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setSemesterID(int semesterID) {
        this.semesterID = semesterID;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public void setMappedGrade(int mappedGrade) {
        this.mappedGrade = mappedGrade;
    }

    public void setGradeLetter(String gradeLetter) {
        this.gradeLetter = gradeLetter;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getCourseID() {
        return courseID;
    }

    public int getSemesterID() {
        return semesterID;
    }

    public float getGrade() {
        return grade;
    }

    public int getMappedGrade() {
        return mappedGrade;
    }

    public String getGradeLetter() {
        return gradeLetter;
    }
    
    
    

    
}
