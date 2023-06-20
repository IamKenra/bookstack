package cont;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.config.cConfig;


public class loginpagecontroller implements Initializable {
    @FXML
    private PasswordField pass;

    @FXML
    private TextField uname;

    @FXML 
    private Button logbtn;

    @FXML
    private Button closebtn;

    @FXML
    private Button daftarbtn;

    @FXML
    private Node rootNode; // Tambahkan ini pada class

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private void loginbtn (ActionEvent event){
        String username = uname.getText();
        String password = pass.getText();

        String encryptedPassword = encryptPassword(password);

        if (encryptedPassword != null) {
            if (authenticate(username, encryptedPassword)) {
                showInfoAlert("Login Success", "User logged in successfully");
                // Lakukan tindakan sesuai kebutuhan setelah login sukses
            } else {
                showErrorAlert("Login Failed", "Invalid username or password");
            }
        } else {
            showErrorAlert("Login Failed", "Failed to encrypt password");
        }
    }
    
    @FXML
    public void daftar() throws IOException {
        if (daftarbtn.getScene() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/daftarpage.fxml"));
                Parent root = loader.load();

                cont.signupcontroller signupController = loader.getController();
                signupController.setStage((Stage) daftarbtn.getScene().getWindow());

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                
                //menutup loginpage.fxml setelah daftarpage.fxml ditampilkan
                Stage loginStage = (Stage) daftarbtn.getScene().getWindow();
                loginStage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
            else {
            System.err.println("Scene is not set. Please make sure the button is attached to a scene.");
            }
    }


   
    @FXML
    public void close() {
        stage.close();
    }

    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = cConfig.connect;
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next(); //autentikasi sukses jika baris hasil query ditemukan

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Autentikasi gagal
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
}
