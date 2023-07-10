package cont;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.config.cConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.ObjectProperty;

public class anggotacontroller {
    @FXML
    private Button daftar, ubah;

    @FXML
    private TableView<Anggota> tableView;

    @FXML
    private TableColumn<Anggota, String> nomorAnggota;

    @FXML
    private TableColumn<Anggota, String> nama;

    @FXML
    private TableColumn<Anggota, Long> no_telepon;

    @FXML
    private TableColumn<Anggota, String> alamat;

    @FXML
    private void initialize() {
        // Menghubungkan kolom dengan properti dalam model Anggota
        nomorAnggota.setCellValueFactory(cellData -> cellData.getValue().nomorAnggotaProperty());
        nama.setCellValueFactory(cellData -> cellData.getValue().namaProperty());
        no_telepon.setCellValueFactory(cellData -> cellData.getValue().noTeleponProperty().asObject());
        alamat.setCellValueFactory(cellData -> cellData.getValue().alamatProperty());
        
        // Menampilkan data anggota ke TableView
        updateTableView();
    }

    @FXML
    private void daftar(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addanggota.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Daftar Anggota");

            // Set the modality to APPLICATION_MODAL to make the new window modal
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTableView();
    }

    @FXML
    private void ubah(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ubahanggota.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ubah Anggota");

            // Set the modality to APPLICATION_MODAL to make the new window modal
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTableView();
    }

    private void updateTableView() {
        // Mendapatkan data anggota dari database
        ObservableList<Anggota> anggotaList = fetchAnggotaData();

        // Menampilkan data anggota ke TableView
        tableView.setItems(anggotaList);
    }

    private ObservableList<Anggota> fetchAnggotaData() {
        ObservableList<Anggota> anggotaList = FXCollections.observableArrayList();

        try {
            cConfig.connection();

            String query = "SELECT * FROM anggota";
            PreparedStatement statement = cConfig.connect.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nomorAnggota = resultSet.getString("nomor_anggota");
                String nama = resultSet.getString("nama");
                long noTelepon = resultSet.getLong("nomor_telepon");
                String alamat = resultSet.getString("alamat");

                Anggota anggota = new Anggota(nomorAnggota, nama, noTelepon, alamat);
                anggotaList.add(anggota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat mengambil data anggota dari database");
        } finally {
            cConfig.disconnect();
        }

        return anggotaList;
    }
}
