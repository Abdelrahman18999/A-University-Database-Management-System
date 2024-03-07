/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package universitydb;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Abdelrahman
 */
public class FXMLDocumentSemesterController implements Initializable {
    
@FXML
    private TextField student_id_tf;

    @FXML
    private TextField semester_id_tf;

    @FXML
    private TextField course_id_tf;

    @FXML
    private TextField grade_tf;

    @FXML
    private TextField mapped_grade_tf;

    @FXML
    private TextField grade_letter_tf;

    @FXML
    private TableView<Courses> tableData;

    @FXML
    private TableColumn<Courses, Integer> student_id_col;

    @FXML
    private TableColumn<Courses, Integer> semester_id_col;

    @FXML
    private TableColumn<Courses, Integer> course_id_col;

    @FXML
    private TableColumn<Courses, Float> grade_col;

    @FXML
    private TableColumn<Courses, Integer> mapped_grade_col;

    @FXML
    private TableColumn<Courses, String> grade_letter_col;
    
    
    int index = -1;
    
    @FXML
    private Button INSERT_btn;
    @FXML
    private Button UPDATE_btn;
    @FXML
    private Button DELETE_btn;
     @FXML
    private Button back_btn;
     
@FXML
private void goBack(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
}

    
    
    @Override
public void initialize(URL url, ResourceBundle rb) {
    showCourses();
}
  
    
    public Connection getConnection(){
        Connection con;
        try{
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "university", "20170427");
            return con;
        } catch(Exception ex){
            System.out.println("Error" + ex.getMessage());
            return null;
        }
    }
    
    public void showCourses() {
    ObservableList<Courses> coursesList = getCourseslist();

    student_id_col.setCellValueFactory(new PropertyValueFactory<>("studentID"));
    semester_id_col.setCellValueFactory(new PropertyValueFactory<>("courseID"));
    course_id_col.setCellValueFactory(new PropertyValueFactory<>("semesterID"));
    grade_col.setCellValueFactory(new PropertyValueFactory<>("grade"));
    mapped_grade_col.setCellValueFactory(new PropertyValueFactory<>("mappedGrade"));
    grade_letter_col.setCellValueFactory(new PropertyValueFactory<>("gradeLetter"));

    tableData.setItems(coursesList);
}


    
    ObservableList<Courses> getCourseslist(){
        ObservableList<Courses> coursesList = FXCollections.observableArrayList();
        Connection con = getConnection();
        String query = "SELECT * FROM STUDENT_SEMESTER_COURSES";
        Statement st;
        ResultSet rs;
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
            Courses course;
            while (rs.next()){
                course = new Courses(rs.getInt("student_id"), 
                                        rs.getInt("semester_id"),
                                        rs.getInt("course_id"),
                                        rs.getFloat("grade"),
                                        rs.getInt("mapped_grade"),
                                        rs.getString("grade_letter"));
                                        
                coursesList.add(course);
            }
        } catch (Exception ex){
              ex.printStackTrace();
        }
        
        return coursesList;
    }
    
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent event){
        index = tableData.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        
        student_id_tf.setText(student_id_col.getCellData(index).toString());
        semester_id_tf.setText(semester_id_col.getCellData(index).toString());
        course_id_tf.setText(course_id_col.getCellData(index).toString());
        grade_tf.setText(grade_col.getCellData(index).toString());
        mapped_grade_tf.setText(mapped_grade_col.getCellData(index).toString());
        grade_letter_tf.setText(grade_letter_col.getCellData(index).toString());
    }

    

    @FXML
private void update(ActionEvent event) throws SQLException {
    Connection con = getConnection();
    String value1 = student_id_tf.getText();
    String value2 = semester_id_tf.getText();
    String value3 = course_id_tf.getText();
    String value4 = grade_tf.getText(); 
    String value5 = mapped_grade_tf.getText();
    String value6 = grade_letter_tf.getText();
    
    // Parse integer values
    int studentId = Integer.parseInt(value1);
    int semesterId = Integer.parseInt(value2);
    int courseId = Integer.parseInt(value3);

    // Parse float value
    float grade = Float.parseFloat(value4);

    /*// Check if the new values already exist in the database
    if (recordExists(con, studentId, semesterId, courseId)) {
        JOptionPane.showMessageDialog(null, "Record with the specified values already exists. Please provide unique values.");
        return;
    }*/

    PreparedStatement ps;
    String updateSQL = "UPDATE STUDENT_SEMESTER_COURSES SET grade=?, mapped_grade=?, grade_letter=? WHERE student_id=? AND course_id=? AND semester_id=?";
    ps = con.prepareStatement(updateSQL);
    
    // Set values using appropriate setter methods
    ps.setFloat(1, grade);
    ps.setString(2, value5);
    ps.setString(3, value6);
    ps.setInt(4, studentId);
    ps.setInt(5, courseId);
    ps.setInt(6, semesterId);
    

    ps.executeUpdate();

    JOptionPane.showMessageDialog(null, "Updated Successfully ^_^");
    reloadScene();
    clearFields();
}

// Check if a record with the specified values already exists
private boolean recordExists(Connection con, int studentId, int semesterId, int courseId) throws SQLException {
    String query = "SELECT * FROM STUDENT_SEMESTER_COURSES WHERE student_id=? AND semester_id=? AND course_id=?";
    try (PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, studentId);
        ps.setInt(2, semesterId);
        ps.setInt(3, courseId);

        try (ResultSet rs = ps.executeQuery()) {
            return rs.next(); // Returns true if a record with the specified values already exists
        }
    }
}

    
    

@FXML
private void insert(ActionEvent event) {
    try {
        Connection con = getConnection();
        String value1 = student_id_tf.getText();
        String value2 = semester_id_tf.getText();
        String value3 = course_id_tf.getText();
        String value4 = grade_tf.getText();
        String value5 = mapped_grade_tf.getText();
        String value6 = grade_letter_tf.getText();

        // Parse integer values
        int studentId = Integer.parseInt(value1);
        int semesterId = Integer.parseInt(value2);
        int courseId = Integer.parseInt(value3);

        // Parse float value
        float grade = Float.parseFloat(value4);

        PreparedStatement ps;
        String insertSQL = "INSERT INTO STUDENT_SEMESTER_COURSES (student_id, semester_id, course_id, grade) VALUES (?, ?, ?, ?)";
        ps = con.prepareStatement(insertSQL);
        ps.setInt(1, studentId);
        ps.setInt(2, semesterId);
        ps.setInt(3, courseId);
        ps.setFloat(4, grade);

        ps.executeUpdate();
        JOptionPane.showMessageDialog(null, "Inserted Successfully ^_^");

        reloadScene();
        // Optional: Clear the input fields after insertion
        clearFields();
    } catch (SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            // Check if it's a unique constraint violation
            SQLIntegrityConstraintViolationException constraintViolationException = (SQLIntegrityConstraintViolationException) e;
            int errorCode = constraintViolationException.getErrorCode();

            if (errorCode == 1) {
                // Unique constraint (ORA-00001) violated
                JOptionPane.showMessageDialog(null, "A record with the specified primary key already exists. Please provide unique values.");
            } else {
                // Handle other SQLIntegrityConstraintViolationException cases if needed
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        } else {
            // Handle other SQLException cases if needed
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}




@FXML
private void delete(ActionEvent event) throws SQLException {
    Connection con = getConnection();
    String value1 = student_id_tf.getText();

    if (value1.isEmpty()) {
        // Handle case where student_id is empty
        JOptionPane.showMessageDialog(null, "Please select a record to delete");
        return;
    }

    int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirmation", JOptionPane.YES_NO_OPTION);
    
    if (confirmation == JOptionPane.YES_OPTION) {
        PreparedStatement ps;
        String deleteSQL = "DELETE FROM STUDENT_SEMESTER_COURSES WHERE student_id=?";
        ps = con.prepareStatement(deleteSQL);
        ps.setString(1, value1);

        int rowsDeleted = ps.executeUpdate();

        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(null, "Record deleted successfully ^_^");
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete record. Please check the student ID.");
        }
    }
    
    reloadScene();
    // Optional: Clear the input fields after deletion
    clearFields();
    
}


// Optional method to clear input fields
private void clearFields() {
    student_id_tf.clear();
    semester_id_tf.clear();
    course_id_tf.clear();
    grade_tf.clear();
    mapped_grade_tf.clear();
    grade_letter_tf.clear();
}

private void reloadScene() {
    // Reload the current scene
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocumentSemesterController.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) tableData.getScene().getWindow(); // Use any node from your current scene
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        // Handle the exception if needed
    }
}


    
}
