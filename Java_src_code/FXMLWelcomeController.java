package universitydb;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class FXMLWelcomeController implements Initializable {
    
    @FXML
    private TextField username_tf;

    @FXML
    private PasswordField password_tf;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }

    @FXML
private void openStudentScene(ActionEvent event) throws IOException {
    // connect to DB
    Connection con = getConnection();
    String username = username_tf.getText();
    String password = password_tf.getText();
    PreparedStatement ps;
    String query = "SELECT * FROM ADMINSTRATORS WHERE USERNAME = ? AND PASSWORD = ?";
    try {
        ps = con.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            // Successful login, open the next scene
            loadScene("FXMLDocument.fxml", "Next Scene Title");
        } else {
            // Invalid credentials, show an alert
            showAlert("Error", "Invalid Credentials", "Please check your username and password.");
        }
    } catch (SQLException ex) {
        //Logger.getLogger(FXMLWelcomeController.class.getName()).log(Level.SEVERE, null, ex);
        Parent logIn = FXMLLoader.load(getClass().getResource("FXMLWelcomeController.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(logIn);
        stage.setScene(scene);
        stage.show();
    }
    // Remove the following lines, as the next scene should only be loaded on successful login
    // Parent logIn = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
    // Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    // Scene scene = new Scene(logIn);
    // stage.setScene(scene);
    // stage.show();
}

    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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


    private void loadScene(String fxmlPath, String title) {
        try {
        Stage popupStage = new Stage();
        Parent students = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(students);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
