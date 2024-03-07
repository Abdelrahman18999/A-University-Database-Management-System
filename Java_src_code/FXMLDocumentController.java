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
import java.sql.Statement;
import java.time.format.DateTimeParseException;
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
import javafx.scene.control.Alert;
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
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private TextField student_id_tf;
    @FXML
    private TextField fname_tf;
    @FXML
    private TextField lname_tf;
    @FXML
    private TextField dof_tf;
    @FXML
    private TextField faculty_tf;
    @FXML
    private TextField major_tf;
    @FXML
    private TextField email_tf;
    @FXML
    private TextField joinDate_tf;
    @FXML
    private TableColumn<Student, Integer> student_id_col;
    @FXML
    private TableColumn<Student, String> fname_col;
    @FXML
    private TableColumn<Student, String> lname_col;
    @FXML
    private TableColumn<Student, String> dob_col;
    @FXML
    private TableColumn<Student, String> faculty_col;
    @FXML
    private TableColumn<Student, String> major_col;
    @FXML
    private TableColumn<Student, String> email_col;
    @FXML
    private TableColumn<Student, String> joinedDate_col;
    @FXML
    private TableView<Student> tableData;
    
    
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
    private Button courses_option;
    @FXML
    private Button dept_option;
    @FXML
    private Button student_gpa_option;
    @FXML
    private Button courses_gpa_option;
    
    ObservableList<Student> studentList;
    
@FXML
    private void SemesterScene(ActionEvent event) throws IOException {
        // Load the semester scene
        Parent logIn = FXMLLoader.load(getClass().getResource("FXMLDocumentSemesterController.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(logIn);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void GPAScene(ActionEvent event) throws IOException {
        // Load the semester scene
        Parent logIn = FXMLLoader.load(getClass().getResource("FXMLDocumentGPA.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(logIn);
        stage.setScene(scene);
        stage.show();
        /*Stage popupStage = new Stage();
        Parent students = FXMLLoader.load(getClass().getResource("FXMLDocumentGPA.fxml"));
        Scene scene = new Scene(students);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();*/
    }
    
    @FXML
    private void DepartmentsScene(ActionEvent event) throws IOException {
        // Load the semester scene
        Parent logIn = FXMLLoader.load(getClass().getResource("Departments.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(logIn);
        stage.setScene(scene);
        stage.show();
    }
    
    
    @FXML
    private void AVGCoursesScene(ActionEvent event) throws IOException {
        // Load the semester scene
        Parent logIn = FXMLLoader.load(getClass().getResource("CoursesGPA.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(logIn);
        stage.setScene(scene);
        stage.show();
    }
    
@FXML
private void goBack(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocumentWelcome.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
}

    
    
    @Override
public void initialize(URL url, ResourceBundle rb) {
    showStudent();
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
    
    public void showStudent() {
    studentList = getStudentlist();

    student_id_col.setCellValueFactory(new PropertyValueFactory<>("studentID"));
    fname_col.setCellValueFactory(new PropertyValueFactory<>("fName"));
    lname_col.setCellValueFactory(new PropertyValueFactory<>("lName"));
    dob_col.setCellValueFactory(new PropertyValueFactory<>("DOB"));
    faculty_col.setCellValueFactory(new PropertyValueFactory<>("faculty"));
    major_col.setCellValueFactory(new PropertyValueFactory<>("major"));
    email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
    joinedDate_col.setCellValueFactory(new PropertyValueFactory<>("joinedDate"));

    tableData.setItems(studentList);
}


    
    ObservableList<Student> getStudentlist(){
        ObservableList<Student> studentList = FXCollections.observableArrayList();
        Connection con = getConnection();
        String query = "SELECT * FROM STUDENT";
        Statement st;
        ResultSet rs;
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
            Student student;
            while (rs.next()){
                student = new Student(rs.getInt("student_id"), 
                                        rs.getString("fname"),
                                        rs.getString("lname"),
                                        rs.getString("DOB"),
                                        rs.getString("faculty"),
                                        rs.getString("major"),
                                        rs.getString("email"),
                                        rs.getString("joined_date"));
                studentList.add(student);
            }
        } catch (Exception ex){
              ex.printStackTrace();
        }
        
        return studentList;
    }
    
    @FXML
public void getSelected(javafx.scene.input.MouseEvent event) {
    index = tableData.getSelectionModel().getSelectedIndex();
    if (index <= -1) {
        return;
    }

    student_id_tf.setText(student_id_col.getCellData(index).toString());
    fname_tf.setText(fname_col.getCellData(index).toString());
    lname_tf.setText(lname_col.getCellData(index).toString());

    // Display DOB without timestamp
    String dob = dob_col.getCellData(index).toString().split(" ")[0];
    dof_tf.setText(dob);

    faculty_tf.setText(faculty_col.getCellData(index).toString());
    major_tf.setText(major_col.getCellData(index).toString());
    email_tf.setText(email_col.getCellData(index).toString());

    // Display Joined Date without timestamp
    String joinedDate = joinedDate_col.getCellData(index).toString().split(" ")[0];
    joinDate_tf.setText(joinedDate);
}


    
@FXML
private void update(ActionEvent event) {
    Connection con = getConnection();

    // Retrieve values from text fields
    String studentId = student_id_tf.getText();
    String firstName = fname_tf.getText();
    String lastName = lname_tf.getText();
    String dob = dof_tf.getText().trim();
    String faculty = faculty_tf.getText();
    String major = major_tf.getText();
    String email = email_tf.getText();
    String joinDate = joinDate_tf.getText().trim();

    // Validate date formats for DOB and Joined Date
    if (!isValidDateFormat(dob) || !isValidDateFormat(joinDate)) {
        showAlert("Invalid date format. Please enter dates in 'YYYY/MM/DD' format.");
        clearFields();
        return;
    }

    // Check data type for Student ID
    try {
        int studentIdValue = Integer.parseInt(studentId);
    } catch (NumberFormatException e) {
        showAlert("Invalid student ID. Please enter a valid integer.");
        clearFields();
        return;
    }

    // Check lengths for VARCHAR2(50) fields
    if (firstName.length() > 50 || lastName.length() > 50 || faculty.length() > 50 || major.length() > 50) {
        showAlert("Input length exceeds the maximum allowed (50 characters) for Faculty, First Name, Last Name, or Major.");
        clearFields();
        return;
    }

    // Check length for email (VARCHAR2(100))
    if (email.length() > 100) {
        showAlert("Email length exceeds the maximum allowed (100 characters).");
        clearFields();
        return;
    }

    // Update the database with the new values
    String updateSQL = "UPDATE STUDENT SET fname=?, lname=?, DOB=?, faculty=?, major=?, email=?, joined_date=? WHERE student_id=?";
    try (PreparedStatement ps = con.prepareStatement(updateSQL)) {
        ps.setString(1, firstName);
        ps.setString(2, lastName);
        ps.setDate(3, java.sql.Date.valueOf(dob));
        ps.setString(4, faculty);
        ps.setString(5, major);
        ps.setString(6, email);
        ps.setDate(7, java.sql.Date.valueOf(joinDate));
        ps.setString(8, studentId);

        int rowsUpdated = ps.executeUpdate();

        if (rowsUpdated > 0) {
            // Refresh the table and show a success message
            showStudent();
            showAlert("Record updated successfully ^_^");
            clearFields();
        } else {
            showAlert("Failed to update record. Please check the student ID.");
            clearFields();
        }
    } catch (SQLException e) {
        if (e.getSQLState().equals("23000") && e.getErrorCode() == 1) {
            // SQLIntegrityConstraintViolationException (unique constraint violation)
            showAlert("Error updating record. The email is already associated with another student. Please enter a unique email.");
        } else {
            showAlert("Error updating record. Please try again.");
        }
        e.printStackTrace(); // Log the exception for debugging purposes
    }
}






@FXML
private void insert(ActionEvent event) {
    try {
        Connection con = getConnection();
        String value1 = student_id_tf.getText();
        String value2 = fname_tf.getText();
        String value3 = lname_tf.getText();
        String value4 = dof_tf.getText().trim();
        String value5 = faculty_tf.getText();
        String value6 = major_tf.getText();
        String value7 = email_tf.getText();
        String value8 = joinDate_tf.getText().trim();

        // Check if student_id is a valid integer
        try {
            int studentId = Integer.parseInt(value1);
            // Check if the ID already exists (Primary Key constraint)
            if (studentIdExists(con, studentId)) {
                showAlert("Student ID already exists. Please enter a unique ID.");
                clearFields();
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid student ID. Please enter a valid integer.");
            clearFields();
            return;
        }

        // Check lengths for VARCHAR2(50) fields
        if (value2.length() > 50 || value3.length() > 50 || value5.length() > 50 || value6.length() > 50) {
            showAlert("Input length exceeds the maximum allowed (50 characters) for Faculty, First Name, Last Name, or Major.");
            clearFields();
            return;
        }

        // Check length for email (VARCHAR2(100))
        if (value7.length() > 100) {
            showAlert("Email length exceeds the maximum allowed (100 characters).");
            clearFields();
            return;
        }
        
        // Check if the email is unique
        if (!isEmailUnique(con, value7)) {
            showAlert("Email already exists. Please enter a unique email.");
            clearFields();
            return;
        }
        
        // Validate date formats for DOB and Joined Date
        if (!isValidDateFormat(value4) || !isValidDateFormat(value8)) {
            showAlert("Invalid date format. Please enter dates in 'YYYY/MM/DD' format.");
            clearFields();
            return;
        }

        //int selectedIndex = tableData.getSelectionModel().getSelectedIndex();
        //(int studentID, String fName, String lName, String DOB, String faculty, String major, String email, String joinedDate
        Student newStudent = new Student(Integer.parseInt(value1), value2, value3, value4, value5, value6, value7, value8);
            studentList.add(newStudent);
            tableData.refresh();
        

        PreparedStatement ps;
        String insertSQL = "INSERT INTO STUDENT (student_id, fname, lname, DOB, faculty, major, email, joined_date) " +
                "VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'))";
        ps = con.prepareStatement(insertSQL);
        ps.setString(1, value1);
        ps.setString(2, value2);
        ps.setString(3, value3);

        try {
            // Try parsing the date values
            ps.setString(4, value4 + " 00:00:00"); // Assuming the date is in 'YYYY-MM-DD' format
            ps.setString(8, value8 + " 00:00:00"); // Assuming the date is in 'YYYY-MM-DD' format
        } catch (DateTimeParseException e) {
            // Invalid date format entered
            showAlert("Invalid date format. Please enter a valid date in 'YYYY-MM-DD' format.");
            clearFields();
            return;
        }

        ps.setString(5, value5);
        ps.setString(6, value6);
        ps.setString(7, value7);

        try {
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Inserted Successfully ^_^");
            //reloadScene();
            //clearFields();
        } catch (SQLException e) {
            // Handle other SQL exceptions
            showAlert("Error inserting data. Please try again.");
            clearFields();
        }

    } catch (SQLException e) {
        // Handle database connection issues
        showAlert("Database connection error. Please try again.");
        e.printStackTrace(); // Log the exception for debugging purposes
    }
}

// Check if the date format is 'YYYY/MM/DD'
private boolean isValidDateFormat(String date) {
    try {
        java.sql.Date.valueOf(date);
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    } catch (IllegalArgumentException e) {
        return false;
    }
}



// Check if the email already exists in the database
private boolean isEmailUnique(Connection con, String email) {
    try {
        String query = "SELECT COUNT(*) FROM STUDENT WHERE email=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Log the exception for debugging purposes
    }
    return false;
}

// Check if the student ID already exists in the database
private boolean studentIdExists(Connection con, int studentId) {
    try {
        String query = "SELECT COUNT(*) FROM STUDENT WHERE student_id=?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Log the exception for debugging purposes
    }
    return false;
}


private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}





@FXML
private void delete(ActionEvent event) throws SQLException {
    Connection con = getConnection();
    String studentIdToDelete = student_id_tf.getText();

    if (studentIdToDelete.isEmpty()) {
        // Handle case where student_id is empty
        showAlert("Please select a record to delete");
        return;
    }

    // Check for related records in other tables before deletion
    if (hasRelatedRecords(con, studentIdToDelete)) {
        showAlert("Cannot delete the student. Related records exist in other tables.");
        return;
    }

    int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirmation", JOptionPane.YES_NO_OPTION);

    if (confirmation == JOptionPane.YES_OPTION) {
        try {
            PreparedStatement ps;
            String deleteSQL = "DELETE FROM STUDENT WHERE student_id=?";
            ps = con.prepareStatement(deleteSQL);
            ps.setString(1, studentIdToDelete);

            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                // Remove the deleted item from the ObservableList
                tableData.getItems().remove(tableData.getSelectionModel().getSelectedIndex());
                tableData.refresh();
                showAlert("Record deleted successfully ^_^");
                clearFields();
            } else {
                showAlert("Failed to delete record. Please check the student ID.");
                clearFields();
            }
        } catch (SQLException e) {
            showAlert("Error deleting record. Please try again.");
            e.printStackTrace(); // Log the exception for debugging purposes
        }
    }
}

    /*Connection con = getConnection();
    String value1 = student_id_tf.getText();

    if (value1.isEmpty()) {
        // Handle case where student_id is empty
        JOptionPane.showMessageDialog(null, "Please select a record to delete");
        return;
    }

    int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirmation", JOptionPane.YES_NO_OPTION);

    if (confirmation == JOptionPane.YES_OPTION) {
        PreparedStatement ps;
        String deleteSQL = "DELETE FROM STUDENT WHERE student_id=?";
        ps = con.prepareStatement(deleteSQL);
        ps.setString(1, value1);

        int rowsDeleted = ps.executeUpdate();

        if (rowsDeleted > 0) {
            // Remove the deleted item from the ObservableList
            tableData.getItems().remove(tableData.getSelectionModel().getSelectedIndex());
            tableData.refresh();
            JOptionPane.showMessageDialog(null, "Record deleted successfully ^_^");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete record. Please check the student ID.");
            clearFields();
        }
    }*/

// Check if there are related records in other tables
// Check if there are related records in other tables
private boolean hasRelatedRecords(Connection con, String studentId) {
    try {
        // Check in STUDENT_SEMESTER_COURSES
        String semesterCoursesQuery = "SELECT COUNT(*) FROM STUDENT_SEMESTER_COURSES WHERE student_id=?";
        try (PreparedStatement ps1 = con.prepareStatement(semesterCoursesQuery)) {
            ps1.setString(1, studentId);
            try (ResultSet rs1 = ps1.executeQuery()) {
                if (rs1.next() && rs1.getInt(1) > 0) {
                    return true; // Related record found in STUDENT_SEMESTER_COURSES
                }
            }
        }

        // Check in STUDENT_GPA
        String gpaQuery = "SELECT COUNT(*) FROM STUDENT_GPA WHERE student_id=?";
        try (PreparedStatement ps2 = con.prepareStatement(gpaQuery)) {
            ps2.setString(1, studentId);
            try (ResultSet rs2 = ps2.executeQuery()) {
                return rs2.next() && rs2.getInt(1) > 0; // Related record found in STUDENT_GPA
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Log the exception for debugging purposes
    }
    return false;
}




// Optional method to clear input fields
private void clearFields() {
    student_id_tf.clear();
    fname_tf.clear();
    lname_tf.clear();
    dof_tf.clear();
    faculty_tf.clear();
    major_tf.clear();
    email_tf.clear();
    joinDate_tf.clear();
}

/*private void reloadScene() {
    // Reload the current scene
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocumentController.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) tableData.getScene().getWindow(); // Use any node from your current scene
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        // Handle the exception if needed
    }
}*/
    
}
