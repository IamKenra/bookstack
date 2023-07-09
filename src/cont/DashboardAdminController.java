package cont;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;




public class DashboardAdminController implements Initializable {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }

    @FXML
    private Button dashboard;

    @FXML
    private BorderPane pane;

    @FXML
    private void dashboard (ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("/fxml/dashboardint.fxml"));
        pane.setCenter(view);
    }

    @FXML
    private void anggota (ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("/fxml/anggota.fxml"));
        pane.setCenter(view);
    }

    @FXML
    private void buku (ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("/fxml/bukupage.fxml"));
        pane.setCenter(view);
    }

    @FXML
    private void peminjaman (ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("/fxml/peminjaman.fxml"));
        pane.setCenter(view);
    }
    
  
}
