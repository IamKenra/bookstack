package cont;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.config.cConfig;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import cont.Buku;


public class bukupagecontroller implements Initializable{
    
    @FXML
    private Button tombol;

    @FXML
    private TableView<Buku> tableView;

    @FXML
    private TableColumn<Buku, String> namaBukuColumn;

    @FXML
    private TableColumn<Buku, String> penulisColumn;

    @FXML
    private TableColumn<Buku, Integer> tahunTerbitColumn;

    @FXML
    private TableColumn<Buku, String> isbnColumn;

    @FXML
    private TableColumn<Buku, Integer> nomorRakIdColumn;

    @FXML
    private TableColumn<Buku, Integer> jumlahColumn;

    @FXML
    private TableColumn<Buku, String> statusColumn;

    
    @FXML
    private void addbuku (ActionEvent event) throws IOException{
      try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addbukupage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Buku Page");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ubahbuku (ActionEvent event) throws IOException{
      try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubahbuku.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ubah Buku Page");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            String query = "SELECT nama_buku, penulis, tahun_terbit, isbn, nomor_rak_id, jumlah, status FROM buku";
            Connection connection = cConfig.connect;
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String namaBuku = resultSet.getString("nama_buku");
                String penulis = resultSet.getString("penulis");
                int tahunTerbit = resultSet.getInt("tahun_terbit");
                String isbn = resultSet.getString("isbn");
                int nomorRakId = resultSet.getInt("nomor_rak_id");
                int jumlah = resultSet.getInt("jumlah");
                String status = resultSet.getString("status");

                Buku buku = new Buku(namaBuku, penulis, tahunTerbit, isbn, nomorRakId, jumlah, status);
                tableView.getItems().add(buku);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cConfig.connection();
        // TODO Auto-generated method stub
        // Set PropertyValueFactory for each column
        namaBukuColumn.setCellValueFactory(new PropertyValueFactory<>("namaBuku"));
        penulisColumn.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        tahunTerbitColumn.setCellValueFactory(new PropertyValueFactory<>("tahunTerbit"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        nomorRakIdColumn.setCellValueFactory(new PropertyValueFactory<>("nomorRakId"));
        jumlahColumn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data from database and populate TableView
        loadData();
        
    }
}
