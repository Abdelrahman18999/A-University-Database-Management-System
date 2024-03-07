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
public class DepartmentController implements Initializable {
    
    @FXML
    private TableView<departments> tableData;
    
    @FXML
    private TableColumn<departments, Integer> department_id_col;

    @FXML
    private TableColumn<departments, String> department_name_col;
    
    
    int index = -1;
    
    @FXML
    private TextField department_id_tf;

    @FXML
    private TextField department_name_tf;

    @FXML
    private Button insert_btn;

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
    showDepartments();
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
    
    public void showDepartments() {
    ObservableList<departments> departmentsList = getdepartmentslist();

    department_id_col.setCellValueFactory(new PropertyValueFactory<>("department_id"));
    department_name_col.setCellValueFactory(new PropertyValueFactory<>("department_name"));

    tableData.setItems(departmentsList);
}


    
    ObservableList<departments> getdepartmentslist(){
        ObservableList<departments> departmentsList = FXCollections.observableArrayList();
        Connection con = getConnection();
        String query = "SELECT * FROM DEPARTMENT";
        Statement st;
        ResultSet rs;
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
            departments department;
            while (rs.next()){
                department = new departments(rs.getInt("department_id"), 
                                        rs.getString("department_name"));
                                        
                departmentsList.add(department);
            }
        } catch (Exception ex){
              ex.printStackTrace();
        }
        
        return departmentsList;
    }
    
    @FXML
    public void getSelected(javafx.scene.input.MouseEvent event){
        index = tableData.getSelectionModel().getSelectedIndex();
        if(index <= -1){
            return;
        }
        
        department_id_tf.setText(department_id_col.getCellData(index).toString());
        department_name_tf.setText(department_name_col.getCellData(index).toString());
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
        String value1 = department_id_tf.getText();
        String value2 = department_name_tf.getText();

        // Parse integer values
        int departmentID = Integer.parseInt(value1);

        PreparedStatement ps;
        String insertSQL = "INSERT INTO DEPARTMENT (department_id, department_name) VALUES (?, ?)";
        ps = con.prepareStatement(insertSQL);
        ps.setInt(1, departmentID);
        ps.setString(2, value2);

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


// Optional method to clear input fields
private void clearFields() {
    department_id_tf.clear();
    department_name_tf.clear();
}

private void reloadScene() {
    // Reload the current scene
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Departments.fxml"));
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
