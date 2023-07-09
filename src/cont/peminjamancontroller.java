package cont;

import com.config.cConfig;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class peminjamancontroller {
    @FXML
    private TableView<Peminjaman> dipinjam;

    @FXML
    private TableColumn<Peminjaman, Integer> idColumn;

    @FXML
    private TableColumn<Peminjaman, String> isbnColumn;

    @FXML
    private TableColumn<Peminjaman, String> namabukucol;//kolom nama buku

    @FXML
    private TableColumn<Peminjaman, String> nomorAnggotaColumn;

    @FXML
    private TableColumn<Peminjaman, String> namapeminjamcol ;

    @FXML
    private TableColumn<Peminjaman, LocalDate> tanggalPeminjamanColumn;

    @FXML
    private TableColumn<Peminjaman, LocalDate> tanggalPengembalianColumn;

    @FXML
    private TableColumn<Peminjaman, String> status ;

   @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomorAnggotaColumn.setCellValueFactory(new PropertyValueFactory<>("nomorAnggota"));
        namapeminjamcol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        namabukucol.setCellValueFactory(new PropertyValueFactory<>("buku"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tanggalPeminjamanColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalPeminjaman"));
        tanggalPengembalianColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));
        
        status.setCellValueFactory(cellData -> {
            Peminjaman peminjaman = cellData.getValue();
            LocalDate currentDate = LocalDate.now();
            String statusText = peminjaman.getTanggalPengembalian().isBefore(currentDate) ? "Terlambat" : "Aman";
            return new SimpleStringProperty(statusText);
        });
        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbperpus", "root", "");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM peminjaman");
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int anggotaId = resultSet.getInt("anggota_id");
                int bukuId = resultSet.getInt("buku_id");
                LocalDate tanggalPeminjaman = resultSet.getDate("tanggal_peminjaman").toLocalDate();
                LocalDate tanggalPengembalian = resultSet.getDate("tanggal_pengembalian").toLocalDate();

                String nomorAnggota = getNomorAnggotaById(anggotaId, connection);
                String isbn = getISBNById(bukuId, connection);
                String nama = getNamaById(anggotaId, connection);
                String buku = getBukuById(bukuId, connection);

                Peminjaman peminjaman = new Peminjaman(id, nomorAnggota, isbn, tanggalPeminjaman, tanggalPengembalian);
                peminjaman.setNama(nama);
                peminjaman.setBuku(buku);
                dipinjam.getItems().add(peminjaman);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    @FXML
    private void peminjaman(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addpeminjaman.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Daftar Anggota");

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNomorAnggotaById(int id, Connection connection) {
        String nomorAnggota = null;
        String query = "SELECT nomor_anggota FROM anggota WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                nomorAnggota = resultSet.getString("nomor_anggota");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomorAnggota;
    }

    public String getISBNById(int id, Connection connection) {
        String isbn = null;
        String query = "SELECT isbn FROM buku WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isbn = resultSet.getString("isbn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isbn;
    }

    
    private String getNamaById(int anggotaId, Connection connection) throws SQLException {
        String query = "SELECT nama FROM anggota WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, anggotaId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nama");
                }
            }
        }
        return null;
    }

    private String getBukuById(int bukuId, Connection connection) throws SQLException {
        String query = "SELECT nama_buku FROM buku WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bukuId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("nama_buku");
                }
            }
        }
        return null;
    }

    public int getJumlahPeminjamanBerjalan() {
        int jumlahPeminjaman = 0;
        String query = "SELECT COUNT(*) FROM peminjaman WHERE tanggal_pengembalian >= ?";
        try (Connection connection = cConfig.connect;
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                jumlahPeminjaman = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jumlahPeminjaman;
    }
    
    public int getJumlahPeminjamanTerlambat() {
        int jumlahPeminjamTerlambat = 0;
        String query = "SELECT COUNT(*) FROM peminjaman WHERE tanggal_pengembalian < ?";
        try (Connection connection = cConfig.connect;
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                jumlahPeminjamTerlambat = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jumlahPeminjamTerlambat;
    }
    
}