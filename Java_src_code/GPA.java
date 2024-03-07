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
public class GPA {
    int student_id;
    int semester_id;
    String fname;
    String lname;
    float gpa;
    
    public GPA () {};
    
    public GPA(int student_id, int semester_id, String fname, String lname, float gpa) {
        this.student_id = student_id;
        this.semester_id = semester_id;
        this.fname = fname;
        this.lname = lname;
        this.gpa = gpa;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public void setSemester_id(int semester_id) {
        this.semester_id = semester_id;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getSemester_id() {
        return semester_id;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public float getGpa() {
        return gpa;
    }
    
    
}
