package cont;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class addpeminjamancontroller {
    @FXML
    private TextField isbn;
    @FXML
    private TextField nomoranggota;
    @FXML
    private DatePicker tanggalkembali;
    @FXML
    private Text namabuku;
    @FXML
    private Text namaanggota;
    @FXML
    private Button daftarbtn;
    @FXML
    private Button cariBukubtn;
    @FXML
    private Button cariAnggotabtn;

    private Connection connection; // Tambahkan variabel connection

    @FXML
    public void initialize() {
        daftarbtn.setOnAction(event -> tambahPeminjaman());
        cariBukubtn.setOnAction(event -> cariBuku());
        cariAnggotabtn.setOnAction(event -> cariAnggota());

        // Buat koneksi saat inisialisasi
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/dbperpus";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void cariBuku() {
        String isbnText = isbn.getText();

        try {
            String queryBuku = "SELECT nama_buku FROM buku WHERE isbn = ?";
            PreparedStatement statement = connection.prepareStatement(queryBuku);
            statement.setString(1, isbnText);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String namaBuku = resultSet.getString("nama_buku");
                namabuku.setText("Nama Buku: " + namaBuku);
            } else {
                namabuku.setText("Buku tidak ditemukan");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cariAnggota() {
        String nomorAnggota = nomoranggota.getText();

        try {
            String queryAnggota = "SELECT nama FROM anggota WHERE nomor_anggota = ?";
            PreparedStatement statement = connection.prepareStatement(queryAnggota);
            statement.setString(1, nomorAnggota);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String namaAnggota = resultSet.getString("nama");
                namaanggota.setText("Nama Anggota: " + namaAnggota);
            } else {
                namaanggota.setText("Anggota tidak ditemukan");
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tambahPeminjaman() {
        String isbnText = isbn.getText();
        String nomorAnggota = nomoranggota.getText();
        LocalDate tanggalKembali = tanggalkembali.getValue();
    
        try {
            String queryBuku = "SELECT id, jumlah FROM buku WHERE isbn = ?";
            PreparedStatement statement = connection.prepareStatement(queryBuku);
            statement.setString(1, isbnText);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                int bukuId = resultSet.getInt("id");
                int jumlahBuku = resultSet.getInt("jumlah");
    
                String queryAnggota = "SELECT id FROM anggota WHERE nomor_anggota = ?";
                PreparedStatement anggotaStatement = connection.prepareStatement(queryAnggota);
                anggotaStatement.setString(1, nomorAnggota);
                ResultSet anggotaResultSet = anggotaStatement.executeQuery();
    
                if (anggotaResultSet.next()) {
                    int anggotaId = anggotaResultSet.getInt("id");
    
                    String queryPeminjaman = "INSERT INTO peminjaman (anggota_id, buku_id, tanggal_peminjaman, tanggal_pengembalian) VALUES (?, ?, ?, ?)";
                    PreparedStatement peminjamanStatement = connection.prepareStatement(queryPeminjaman);
                    peminjamanStatement.setInt(1, anggotaId);
                    peminjamanStatement.setInt(2, bukuId);
                    peminjamanStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                    peminjamanStatement.setDate(4, java.sql.Date.valueOf(tanggalKembali));
                    peminjamanStatement.executeUpdate();
    
                    String updateBuku = "UPDATE buku SET jumlah = ? WHERE isbn = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateBuku);
                    updateStatement.setInt(1, jumlahBuku - 1);
                    updateStatement.setString(2, isbnText);
                    updateStatement.executeUpdate();
    
                    peminjamanStatement.close();
                    updateStatement.close();
                } else {
                    namaanggota.setText("Anggota tidak ditemukan");
                }
    
                anggotaResultSet.close();
                anggotaStatement.close();
            } else {
                namabuku.setText("Buku tidak ditemukan");
            }
    
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    // Tutup koneksi saat controller dihancurkan
    public void finalize() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
