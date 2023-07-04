package cont;
import com.config.cConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class signupcontroller {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField uname;

    @FXML
    private PasswordField pass,conpass;

    @FXML
    private Button daftarbtn,closebtn,backbtn;

    
    @FXML
    private void daftar() {
        String username = uname.getText();
        String password = pass.getText();
        String confirmPassword = conpass.getText();

        if (!password.equals(confirmPassword)) {
            showErrorAlert("Registration Failed", "Passwords do not match");
            return;
        }

        String encryptedPassword = encryptPassword(password);

        if (encryptedPassword != null) {
            if (register(username, encryptedPassword)) {
                showInfoAlert("Registration Success", "User registered successfully");
                // Lakukan tindakan sesuai kebutuhan setelah pendaftaran sukses
            } else {
                showErrorAlert("Registration Failed", "Failed to register user");
            }
        } else {
            showErrorAlert("Registration Failed", "Failed to encrypt password");
        }
    }

    @FXML
    public void close() {
        stage.close();
    }

    @FXML
    public void back() throws IOException, InvocationTargetException{
        if (stage != null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginpage.fxml"));
            Parent root = loader.load();

            cont.loginpagecontroller loginController = loader.getController();
            loginController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); // Memperlihatkan daftarpage.fxml

            // Menutup loginpage.fxml setelah daftarpage.fxml ditampilkan
            Stage daftarStage = (Stage) closebtn.getScene().getWindow();
            daftarStage.close();
            } catch (Exception e) {
                e.printStackTrace();
                Throwable cause = e.getCause();
                if (cause != null) {
                    cause.printStackTrace();
                }
            }
            } else {
                System.err.println("Stage is not set. Please set the stage before calling 'daftar()'.");
            }
    }
    
    private boolean register(String username, String password) {
    String query = "INSERT INTO admin (username, password) VALUES (?, ?)";

    try (Connection connection = cConfig.connect;
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, username);
        statement.setString(2, password);

        statement.executeUpdate();

        return true; // Pendaftaran sukses

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            cConfig.connect.close(); // Menutup koneksi basis data
            back(); // Memanggil metode back()
        } catch (SQLException e) {
            System.out.println("Gagal menutup koneksi: " + e.getMessage());
        } catch (IOException | InvocationTargetException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause != null) {
                cause.printStackTrace();
            }
        }
    }

    return false; // Pendaftaran gagal
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
}