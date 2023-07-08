package cont;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.config.cConfig;

public class addanggotacontroller {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField namaLengkap;

    @FXML
    private TextField nomorTlp;

    @FXML
    private Button daftar;

    @FXML
    private TextArea alamat;

    @FXML
    private DatePicker tanggalLahir;

    
    @FXML
    private void daftar() {
        // Generate nomor anggota unik
        String nomorAnggota = generateUniqueNomorAnggota();
        
        // Dapatkan data dari komponen UI
        String nama = namaLengkap.getText();
        String nomorTelepon = nomorTlp.getText();
        String alamatLengkap = alamat.getText();
        LocalDate tanggalLahirDate = tanggalLahir.getValue();
        String tanggalLahirStr = tanggalLahirDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Simpan data ke database
        try {
            cConfig.connection();

            String query = "INSERT INTO anggota (nomor_anggota, nama, nomor_telepon, alamat, tanggal_lahir) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = cConfig.connect.prepareStatement(query);
            statement.setString(1, nomorAnggota);
            statement.setString(2, nama);
            statement.setString(3, nomorTelepon);
            statement.setString(4, alamatLengkap);
            statement.setString(5, tanggalLahirStr);
            statement.executeUpdate();

            System.out.println("Data berhasil disimpan ke database");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat menyimpan data ke database");
        } finally {
            cConfig.disconnect();
        }
    }

    private String generateUniqueNomorAnggota() {
        String nomorAnggota = null;
        boolean unique = false;
        
        try {
            cConfig.connection();
            PreparedStatement statement = cConfig.connect.prepareStatement("SELECT anggota FROM nama_tabel WHERE nomor_anggota = ?");
            
            do {
                nomorAnggota = UUID.randomUUID().toString().substring(0, 8);
                statement.setString(1, nomorAnggota);
                ResultSet resultSet = statement.executeQuery();
                unique = !resultSet.next();
            } while (!unique);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cConfig.disconnect();
        }
        
        return nomorAnggota;
    }
}
