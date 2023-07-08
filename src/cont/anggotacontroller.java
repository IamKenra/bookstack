package cont;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class anggotacontroller {
    @FXML
    private Button daftar,ubah;
    
    @FXML
    private void daftar(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addanggota.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ubah Buku Page");

            // Set the modality to APPLICATION_MODAL to make the new window modal
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ubah(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubahanggota.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ubah Buku Page");

            // Set the modality to APPLICATION_MODAL to make the new window modal
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
