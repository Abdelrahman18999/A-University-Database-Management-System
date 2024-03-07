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
public class CoursesGPAController implements Initializable {
    
    @FXML
    private TableView<CoursesGPA> tableData;

    @FXML
    private TableColumn<CoursesGPA, String> course_name_col;

    @FXML
    private TableColumn<CoursesGPA, Integer> course_id_col;

    @FXML
    private TableColumn<CoursesGPA, Integer> enrollments_col;

    @FXML
    private TableColumn<CoursesGPA, Float> gpa_col;

    @FXML
    private Button show_all_btn;

    @FXML
    private Button by_course_btn;

    @FXML
    private Button back_btn;

    @FXML
    private TextField course_id_tf;
    
    
    int index = -1;
    
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
    //showStudentGPA();
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
    
    public void showCourseGPA() {
    ObservableList<CoursesGPA> courseGPAList = getcourseGPAlist();

    course_name_col.setCellValueFactory(new PropertyValueFactory<>("course_name"));
    course_id_col.setCellValueFactory(new PropertyValueFactory<>("course_id"));
    enrollments_col.setCellValueFactory(new PropertyValueFactory<>("enrollments"));
    gpa_col.setCellValueFactory(new PropertyValueFactory<>("course_gpa"));

    tableData.setItems(courseGPAList);
}


    
    ObservableList<CoursesGPA> getcourseGPAlist(){
        ObservableList<CoursesGPA> courseGPAList = FXCollections.observableArrayList();
        Connection con = getConnection();
        String query = "SELECT COURSE_NAME, ID AS COURSE_ID, ENROLLMENT_STUDENTS, " +
        "ROUND(SUM(MAPPED_GRADE * CREDITS) / SUM(CREDITS), 2) AS COURSE_GPA " +
        "FROM " +
        "(SELECT SS.COURSE_ID, SS.STUDENT_ID, SS.MAPPED_GRADE, S.COURSE_NAME, S.CREDITS, " +
        "SS.COURSE_ID AS ID, COUNT(SS.STUDENT_ID) OVER(PARTITION BY SS.COURSE_ID) AS ENROLLMENT_STUDENTS " +
        "FROM STUDENT_SEMESTER_COURSES SS INNER JOIN COURSES S ON SS.COURSE_ID = S.COURSE_ID) " +
        " GROUP BY COURSE_NAME, ENROLLMENT_STUDENTS, ID";

        Statement st;
        ResultSet rs;
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
            CoursesGPA coursegpa;
            while (rs.next()){
                coursegpa = new CoursesGPA(rs.getString("COURSE_NAME"), 
                                     rs.getInt("COURSE_ID"),
                                     rs.getInt("ENROLLMENT_STUDENTS"),
                                     rs.getFloat("COURSE_GPA"));

                courseGPAList.add(coursegpa);
            }
        } catch (Exception ex){
              ex.printStackTrace();
        }
        
        return courseGPAList;
    }
    
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent event){
        index = tableData.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        
        course_id_tf.setText(course_id_col.getCellData(index).toString());
    }

    
@FXML
private void gpaByCourse(ActionEvent event) throws SQLException {
    Connection con = getConnection();
    String value1 = course_id_tf.getText();

    PreparedStatement ps;
    String query = "SELECT COURSE_NAME, ID AS COURSE_ID, ENROLLMENT_STUDENTS, ROUND(SUM(MAPPED_GRADE * CREDITS) / SUM(CREDITS), 2) AS COURSE_GPA " +
               "FROM  (SELECT SS.COURSE_ID, SS.STUDENT_ID, SS.MAPPED_GRADE, S.COURSE_NAME, S.CREDITS, SS.COURSE_ID AS ID, " +
               "COUNT(SS.STUDENT_ID) OVER(PARTITION BY SS.COURSE_ID) AS ENROLLMENT_STUDENTS " +
               " FROM STUDENT_SEMESTER_COURSES SS " +
               "INNER JOIN COURSES S " +
               "ON SS.COURSE_ID = S.COURSE_ID " +
                "WHERE SS.COURSE_ID = ? )" +
               "GROUP BY COURSE_NAME, ENROLLMENT_STUDENTS, ID ";
    ps = con.prepareStatement(query);
    ps.setString(1, value1);

    ResultSet rs = ps.executeQuery();

    // Use the existing studentGPAList
    ObservableList<CoursesGPA> courseGPAList = FXCollections.observableArrayList();
    CoursesGPA coursegpa;
    while (rs.next()){
                coursegpa = new CoursesGPA(rs.getString("COURSE_NAME"), 
                                     rs.getInt("COURSE_ID"),
                                     rs.getInt("ENROLLMENT_STUDENTS"),
                                     rs.getFloat("COURSE_GPA"));

                courseGPAList.add(coursegpa);
    }

    // Set the new items to the existing studentGPAList
    tableData.getItems().clear();
    tableData.setItems(courseGPAList);
    tableData.refresh();

    // Optional: clear input fields
    clearFields();
    //reloadScene();
}





// Optional method to clear input fields
private void clearFields() {
    course_id_tf.clear();
}

private void reloadScene() {
    try {
        // Load the FXMLLoader with the current FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CoursesGPA.fxml"));

        // Load the root (Parent) from the FXMLLoader
        Parent root = loader.load();

        // Get the controller instance from the FXMLLoader
        FXMLGPAController controller = loader.getController();

        // Reload the TableView using the showStudentGPA method
        controller.showStudentGPA();

        // Set the new root to the current scene
        Scene scene = new Scene(root);
        Stage stage = (Stage) tableData.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
        // Handle the exception if needed
    }
    // Clear the input fields
    clearFields();
}


    
}
