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
public class CoursesGPA {
    
    String course_name;
    int course_id;
    int enrollments;
    float course_gpa;

    public CoursesGPA () {};
    public CoursesGPA(String course_name, int course_id, int enrollments, float course_gpa) {
        this.course_name = course_name;
        this.course_id = course_id;
        this.enrollments = enrollments;
        this.course_gpa = course_gpa;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public void setEnrollments(int enrollments) {
        this.enrollments = enrollments;
    }

    public void setCourse_gpa(float course_gpa) {
        this.course_gpa = course_gpa;
    }

    public String getCourse_name() {
        return course_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public int getEnrollments() {
        return enrollments;
    }

    public float getCourse_gpa() {
        return course_gpa;
    }
    
    
    
}
