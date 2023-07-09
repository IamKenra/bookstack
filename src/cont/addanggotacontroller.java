package cont;

import com.config.cConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addanggotacontroller {
    @FXML
    private TextField txtNomorAnggota;
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
        String nomorAnggota = txtNomorAnggota.getText();
        String nama = txtNama.getText();
        String nomorTelepon = txtNomorTelepon.getText();
        String alamatLengkap = txtAlamat.getText();
        String tanggalLahirStr = dateTanggalLahir.getValue().toString();

        if (nomorAnggota.isEmpty() || nama.isEmpty() || nomorTelepon.isEmpty() || alamatLengkap.isEmpty() || tanggalLahirStr.isEmpty()) {
            showErrorMessage("Harap isi semua field");
            return;
        }

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
        txtNomorAnggota.clear();
        txtNama.clear();
        txtNomorTelepon.clear();
        txtAlamat.clear();
        dateTanggalLahir.setValue(null);
    }
}
