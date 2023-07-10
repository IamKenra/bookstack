package cont;

import com.config.cConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class addanggotacontroller {
    @FXML
    private TextField txtNama;
    @FXML
    private TextField txtNomorTelepon;
    @FXML
    private TextArea txtAlamat;
    @FXML
    private DatePicker dateTanggalLahir;

    @FXML
    private void daftar() {
        String nama = txtNama.getText();
        String nomorTelepon = txtNomorTelepon.getText();
        String alamatLengkap = txtAlamat.getText();
        String tanggalLahirStr = dateTanggalLahir.getValue().toString();

        if (nama.isEmpty() || nomorTelepon.isEmpty() || alamatLengkap.isEmpty() || tanggalLahirStr.isEmpty()) {
            showErrorMessage("Harap isi semua field");
            return;
        }

        try {
            cConfig.connection();

            // Mendapatkan nomor anggota yang unik dari database
            String queryGetNomorAnggota = "SELECT MAX(nomor_anggota) AS max_nomor FROM anggota";
            PreparedStatement statementGetNomorAnggota = cConfig.connect.prepareStatement(queryGetNomorAnggota);
            ResultSet resultSet = statementGetNomorAnggota.executeQuery();
            int nomorAnggota;
            if (resultSet.next()) {
                nomorAnggota = resultSet.getInt("max_nomor") + 1;
            } else {
                nomorAnggota = 1;
            }

            // Menghasilkan nomor anggota secara random dan unik
            Random random = new Random();
            boolean unique = false;
            while (!unique) {
                int randomNomor = random.nextInt(1000000); // Ubah angka 1000000 sesuai dengan batas angka maksimal yang diinginkan
                String queryCheckNomorAnggota = "SELECT COUNT(*) AS count FROM anggota WHERE nomor_anggota = ?";
                PreparedStatement statementCheckNomorAnggota = cConfig.connect.prepareStatement(queryCheckNomorAnggota);
                statementCheckNomorAnggota.setInt(1, randomNomor);
                ResultSet resultCheckNomorAnggota = statementCheckNomorAnggota.executeQuery();
                if (resultCheckNomorAnggota.next()) {
                    int count = resultCheckNomorAnggota.getInt("count");
                    if (count == 0) {
                        nomorAnggota = randomNomor;
                        unique = true;
                    }
                }
            }

            // Menyimpan data anggota ke database
            String query = "INSERT INTO anggota (nomor_anggota, nama, nomor_telepon, alamat, tanggal_lahir) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = cConfig.connect.prepareStatement(query);
            statement.setInt(1, nomorAnggota);
            statement.setString(2, nama);
            statement.setString(3, nomorTelepon);
            statement.setString(4, alamatLengkap);
            statement.setString(5, tanggalLahirStr);
            statement.executeUpdate();

            showInformationMessage("Data berhasil disimpan ke database");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Terjadi kesalahan saat menyimpan data ke database");
        } finally {
            cConfig.disconnect();
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtNama.clear();
        txtNomorTelepon.clear();
        txtAlamat.clear();
        dateTanggalLahir.setValue(null);
    }
}
