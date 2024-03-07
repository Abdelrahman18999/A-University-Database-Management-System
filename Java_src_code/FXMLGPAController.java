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
public class FXMLGPAController implements Initializable {
    
    @FXML
    private TableView<GPA> gpaTable;
    
    @FXML
    private TableColumn<GPA, Integer> id_col;

    @FXML
    private TableColumn<GPA, String> fname_col;

    @FXML
    private TableColumn<GPA, String> lname_col;

    @FXML
    private TableColumn<GPA, Integer> semester_id_col;

    @FXML
    private TableColumn<GPA, Float> gpa_col;

    @FXML
    private TextField student_id_tf;

    @FXML
    private TextField semester_id_tf;

    @FXML
    private Button by_student_btn;

    //@FXML
    // private Button all_students_btn;

    @FXML
    private Button back_btn;
    
    @FXML
    private Button showALLbtn;
    
    @FXML
    private TextField avg_gpa_tf;
    
    
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
    
    public void showStudentGPA() {
    ObservableList<GPA> studentGPAList = getStudentGPAlist();

    id_col.setCellValueFactory(new PropertyValueFactory<>("student_id"));
    fname_col.setCellValueFactory(new PropertyValueFactory<>("fname"));
    lname_col.setCellValueFactory(new PropertyValueFactory<>("lname"));
    semester_id_col.setCellValueFactory(new PropertyValueFactory<>("semester_id"));
    gpa_col.setCellValueFactory(new PropertyValueFactory<>("gpa"));

    gpaTable.setItems(studentGPAList);
}


    
    ObservableList<GPA> getStudentGPAlist(){
        ObservableList<GPA> studentGPAList = FXCollections.observableArrayList();
        Connection con = getConnection();
        String query = "SELECT c.student_id, s.fname, s.lname, c.semester_id, calc_gpa(c.student_id, c.semester_id) as GPA " +
               "FROM STUDENT_SEMESTER_COURSES c " +
               "INNER JOIN STUDENT s ON c.STUDENT_ID = s.STUDENT_ID " +
               "GROUP BY c.student_id, c.semester_id, s.fname, s.lname";
        Statement st;
        ResultSet rs;
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
            GPA studentgpa;
            while (rs.next()){
                studentgpa = new GPA(rs.getInt("student_id"), 
                                     rs.getInt("semester_id"),
                                     rs.getString("fname"),
                                     rs.getString("lname"),
                                     rs.getFloat("GPA"));

                studentGPAList.add(studentgpa);
            }
        } catch (Exception ex){
              ex.printStackTrace();
        }
        
        return studentGPAList;
    }
    
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent event){
        index = gpaTable.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        
        student_id_tf.setText(id_col.getCellData(index).toString());
        semester_id_tf.setText(semester_id_col.getCellData(index).toString());
    }

    
@FXML
private void gpaByStudent(ActionEvent event) throws SQLException {
    Connection con = getConnection();
    String value1 = student_id_tf.getText();

    PreparedStatement ps;
    String query = "SELECT c.student_id, s.fname, s.lname, c.semester_id, calc_gpa(c.student_id, c.semester_id) as GPA " +
               "FROM STUDENT_SEMESTER_COURSES c " +
               "INNER JOIN STUDENT s ON c.STUDENT_ID = s.STUDENT_ID " +
               "WHERE c.student_id = ?";
    ps = con.prepareStatement(query);
    ps.setString(1, value1);

    ResultSet rs = ps.executeQuery();

    // Use the existing studentGPAList
    ObservableList<GPA> studentGPAList = FXCollections.observableArrayList();

    while (rs.next()) {
        GPA gpa = new GPA(rs.getInt("student_id"), 
                                 rs.getInt("semester_id"),
                                 rs.getString("fname"),
                                 rs.getString("lname"),
                                 rs.getFloat("GPA"));
        
        //System.out.println(gpa.fname);
        studentGPAList.add(gpa);
    }

    // Set the new items to the existing studentGPAList
    gpaTable.getItems().clear();
    gpaTable.setItems(studentGPAList);
    gpaTable.refresh();

    // Optional: clear input fields
    clearFields();
    //reloadScene();
}




@FXML
private void avgStudentGPA(ActionEvent event) throws SQLException {
    Connection con = getConnection();
    String value1 = student_id_tf.getText();
    String value2 = semester_id_tf.getText();

    PreparedStatement ps;
    String query = "SELECT calc_avg_gpa(?, ?) AS avg_gpa FROM DUAL"; // DUAL is a dummy table in Oracle
    ps = con.prepareStatement(query);
    ps.setString(1, value1);
    ps.setString(2, value2);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        float avgGPA = rs.getFloat("avg_gpa");
        avg_gpa_tf.setText(String.valueOf(avgGPA));
    }

    // Optional: clear input fields
    clearFields();
}



// Optional method to clear input fields
private void clearFields() {
    student_id_tf.clear();
    semester_id_tf.clear();
}

private void reloadScene() {
    try {
        // Load the FXMLLoader with the current FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocumentGPA.fxml"));

        // Load the root (Parent) from the FXMLLoader
        Parent root = loader.load();

        // Get the controller instance from the FXMLLoader
        FXMLGPAController controller = loader.getController();

        // Reload the TableView using the showStudentGPA method
        controller.showStudentGPA();

        // Set the new root to the current scene
        Scene scene = new Scene(root);
        Stage stage = (Stage) gpaTable.getScene().getWindow();
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
