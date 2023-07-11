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
import com.config.cConfig;

public class ubahanggotacontroller {
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
    private TextField cari;

    @FXML
    private Button hapus;

    @FXML
    private Button caribtn;

    @FXML
    private void cari(){
        String keyword = cari.getText();

        try {
            cConfig.connection();

            String query = "SELECT * FROM anggota WHERE nama = ? OR nomor_anggota = ?";
            PreparedStatement statement = cConfig.connect.prepareStatement(query);
            statement.setString(1, keyword);
            statement.setString(2, keyword);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nama = resultSet.getString("nama");
                String nomorTelepon = resultSet.getString("nomor_telepon");
                String alamatLengkap = resultSet.getString("alamat");
                String tanggalLahirStr = resultSet.getString("tanggal_lahir");

                namaLengkap.setText(nama);
                nomorTlp.setText(nomorTelepon);
                alamat.setText(alamatLengkap);
                tanggalLahir.getEditor().setText(tanggalLahirStr);
            } else {
                System.out.println("Data tidak ditemukan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat mencari data");
        } finally {
            cConfig.disconnect();
        }
    }

    @FXML
    private void ubah() {
        String keyword = cari.getText();
        String nama = namaLengkap.getText();
        String nomorTelepon = nomorTlp.getText();
        String alamatLengkap = alamat.getText();
        String tanggalLahirStr = tanggalLahir.getEditor().getText();

        try {
            cConfig.connection();

            String query = "UPDATE anggota SET nama = ?, nomor_telepon = ?, alamat = ?, tanggal_lahir = ? WHERE nama = ? OR nomor_anggota = ?";
            PreparedStatement statement = cConfig.connect.prepareStatement(query);
            statement.setString(1, nama);
            statement.setString(2, nomorTelepon);
            statement.setString(3, alamatLengkap);
            statement.setString(4, tanggalLahirStr);
            statement.setString(5, keyword);
            statement.setString(6, keyword);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data berhasil diperbarui");
            } else {
                System.out.println("Data tidak ditemukan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat mengupdate data");
        } finally {
            cConfig.disconnect();
        }
    }

    @FXML
    private void hapus() {
        String nama = namaLengkap.getText();
        String nomorTelepon = nomorTlp.getText();
        String alamatLengkap = alamat.getText();
        String tanggalLahirStr = tanggalLahir.getEditor().getText();

        try {
            cConfig.connection();

            String query = "DELETE FROM anggota WHERE nama = ? AND nomor_telepon = ? AND alamat = ? AND tanggal_lahir = ?";
            PreparedStatement statement = cConfig.connect.prepareStatement(query);
            statement.setString(1, nama);
            statement.setString(2, nomorTelepon);
            statement.setString(3, alamatLengkap);
            statement.setString(4, tanggalLahirStr);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data berhasil dihapus dari database");
            } else {
                System.out.println("Data tidak ditemukan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Anggota masih memiliki buku yang dipinjam, tidak bisa menghapus!");
        } finally {
            cConfig.disconnect();
        }

        // Bersihkan nilai-nilai di TextField setelah menghapus data
        namaLengkap.clear();
        nomorTlp.clear();
        alamat.clear();
        tanggalLahir.getEditor().clear();
    }
}
