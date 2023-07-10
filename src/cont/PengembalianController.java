package cont;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import com.config.cConfig;
import java.sql.*;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PengembalianController {
    @FXML
    private TextField nomorAnggota;
    @FXML
    private Button cariBukubtn;
    @FXML
    private Text namabuku;
    @FXML
    private Text namaanggota;
    @FXML
    private TextField namaAnggota;
    @FXML
    private TextField namaBuku;
    @FXML
    private TextField status;

    private Connection connection;
    private PreparedStatement preparedStatement;

    // Method ini akan dipanggil setelah komponen FXML diinisialisasi
    @FXML
    private void initialize() {
        // Menghubungkan ke database
        connectToDatabase();
    }

    // Method untuk menghubungkan ke database
    private void connectToDatabase() {
        try {
            // Panggil method connection() dari class cConfig untuk melakukan koneksi
            cConfig.connection();
            connection = cConfig.connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method untuk menghandle aksi tombol cariBukubtn
    @FXML
    private void cariBuku() {
        // Dapatkan nomor peminjaman dari input nomorAnggota
        String nomorPeminjaman = nomorAnggota.getText();

        // Panggil method pencarianBuku dengan nomorPeminjaman sebagai parameter
        Peminjaman peminjaman = pencarianBuku(nomorPeminjaman);

        // Periksa apakah data peminjaman ditemukan
        if (peminjaman != null) {
            // Tampilkan nama buku dan nama anggota peminjam
            namaBuku.setText(peminjaman.getBuku());
            namaAnggota.setText(peminjaman.getNama());

            // Periksa status pengembalian
            LocalDate tanggalPengembalian = peminjaman.getTanggalPengembalian();
            LocalDate tanggalSistem = LocalDate.now();

            // Bandingkan tanggal pengembalian dengan tanggal sistem
            if (tanggalPengembalian.isBefore(tanggalSistem)) {
                status.setText("Terlambat");
            } else {
                status.setText("Tepat waktu");
            }
        } else {
            // Jika data peminjaman tidak ditemukan, kosongkan teks dan status
            System.out.println("tidak ada data");
            namaBuku.setText("");
            namaAnggota.setText("");
            status.setText("");
        }
    }
    // Method untuk melakukan pencarian buku berdasarkan nomor peminjaman
    // Method untuk melakukan pencarian buku berdasarkan nomor_anggota
private Peminjaman pencarianBuku(String nomorAnggota) {
    try {
        // Query SQL untuk mencari data peminjaman berdasarkan nomor_anggota
        String query = "SELECT peminjaman.id, peminjaman.buku_id, peminjaman.tanggal_peminjaman, peminjaman.tanggal_pengembalian, anggota.nama, buku.nama AS nama_buku, buku.isbn FROM peminjaman JOIN anggota ON peminjaman.anggota_id = anggota.id JOIN buku ON peminjaman.buku_id = buku.id WHERE anggota.nomor_anggota = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nomorAnggota);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Ambil data dari ResultSet
            int id = resultSet.getInt("id");
            int bukuId = resultSet.getInt("buku_id");
            LocalDate tanggalPeminjaman = resultSet.getDate("tanggal_peminjaman").toLocalDate();
            LocalDate tanggalPengembalian = resultSet.getDate("tanggal_pengembalian").toLocalDate();
            String namaAnggota = resultSet.getString("nama");
            String namaBuku = resultSet.getString("nama_buku");
            String isbnBuku = resultSet.getString("isbn");

            // Buat objek Peminjaman dengan data yang ditemukan
            Peminjaman peminjaman = new Peminjaman(id, nomorAnggota, isbnBuku, tanggalPeminjaman, tanggalPengembalian);
            peminjaman.setNama(namaAnggota); // Menggunakan data nama anggota dari tabel anggota
            peminjaman.setBuku(namaBuku); // Menggunakan data nama buku dari tabel buku
            return peminjaman;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}

 
private String getNomorAnggota(int anggotaId) {
    try {
        String query = "SELECT nomor_anggota FROM anggota WHERE id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, anggotaId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("nomor_anggota");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    // Method untuk mendapatkan ISBN buku berdasarkan ID
    private String getISBNBuku(int bukuId) {
        try {
            String query = "SELECT isbn FROM buku WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bukuId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("isbn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method untuk menghapus data peminjaman dari database berdasarkan nomor anggota
    @FXML
    private void hapusPeminjaman() {
        try {
            // Query SQL untuk menghapus data peminjaman berdasarkan nomor anggota
            String query = "DELETE FROM peminjaman WHERE nomor_anggota = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nomorAnggota.getText());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Data peminjaman berhasil dihapus");

                // Peringatan jika buku sudah dikembalikan
                showAlert(AlertType.WARNING, "Buku sudah dikembalikan");
            } else {
                System.out.println("Data peminjaman tidak ditemukan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
