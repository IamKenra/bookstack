package cont;

import com.config.cConfig;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ubahbukucontroller {
    @FXML
    private TextField cariBukuField;

    @FXML
    private TextField namaBukuField;

    @FXML
    private TextField penulisField;

    @FXML
    private TextField tahunTerbitField;

    @FXML
    private TextField isbnField;

    @FXML
    private ComboBox<String> nomorRakComboBox;

    @FXML
    private TextField jumlahField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private void initialize() {
        cConfig.connection();
        statusComboBox.getItems().addAll("Tersedia", "Tidak Tersedia");
    }

    @FXML
    private void cariButtonClicked() {
        String keyword = cariBukuField.getText();
        searchBuku(keyword);

        if (keyword.isEmpty()) {
        showAlert(AlertType.WARNING, "Pencarian Kosong", "Masukkan kata kunci pencarian");
        } else {
            searchBuku(keyword);
        }
    }

    @FXML
    private void simpanButtonClicked() {
        String namaBuku = namaBukuField.getText();
        String penulis = penulisField.getText();
        int tahunTerbit = Integer.parseInt(tahunTerbitField.getText());
        String isbn = isbnField.getText();
        int nomorRakId = Integer.parseInt(nomorRakComboBox.getValue());
        int jumlah = Integer.parseInt(jumlahField.getText());
        String status = statusComboBox.getValue();

        try {
            int bukuId = getBukuId(namaBuku);

            if (bukuId != -1) {
                updateBuku(bukuId, namaBuku, penulis, tahunTerbit, isbn, nomorRakId, jumlah, status);
                System.out.println("Perubahan berhasil disimpan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void hapusButtonClicked() {
        String namaBuku = namaBukuField.getText();

        try {
            int bukuId = getBukuId(namaBuku);

            if (bukuId != -1) {
                deleteBuku(bukuId);
                clearFields();
                System.out.println("Buku berhasil dihapus");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchBuku(String keyword) {
        try {
            String query = "SELECT * FROM buku WHERE nama_buku LIKE ? OR isbn LIKE ?";
            Connection connection = cConfig.connect;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String namaBuku = resultSet.getString("nama_buku");
                String penulis = resultSet.getString("penulis");
                int tahunTerbit = resultSet.getInt("tahun_terbit");
                String isbn = resultSet.getString("isbn");
                int nomorRakId = resultSet.getInt("nomor_rak_id");
                int jumlah = resultSet.getInt("jumlah");
                String status = resultSet.getString("status");

                namaBukuField.setText(namaBuku);
                penulisField.setText(penulis);
                tahunTerbitField.setText(String.valueOf(tahunTerbit));
                isbnField.setText(isbn);
                nomorRakComboBox.setValue(String.valueOf(nomorRakId));
                jumlahField.setText(String.valueOf(jumlah));

                if (status.equals("tersedia")) {
                    statusComboBox.setValue("Tersedia");
                } else {
                    statusComboBox.setValue("Tidak Tersedia");
                }
            } else {
                clearFields();
                showAlert(AlertType.WARNING, "Data tidak ditemukan", "Buku dengan kata kunci tersebut tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBuku(int bukuId, String namaBuku, String penulis, int tahunTerbit, String isbn, int nomorRakId, int jumlah, String status) throws SQLException {
        String query = "UPDATE buku SET penulis = ?, tahun_terbit = ?, isbn = ?, nomor_rak_id = ?, jumlah = ?, status = ? WHERE id = ?";
        Connection connection = cConfig.connect;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, penulis);
        statement.setInt(2, tahunTerbit);
        statement.setString(3, isbn);
        statement.setInt(4, nomorRakId);
        statement.setInt(5, jumlah);
        statement.setString(6, status);
        statement.setInt(7, bukuId);

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Data buku berhasil diperbarui");
        }
    }

    private int getBukuId(String namaBuku) throws SQLException {
        String query = "SELECT id FROM buku WHERE nama_buku = ?";
        Connection connection = cConfig.connect;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, namaBuku);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("id");
        }

        return -1; // Return -1 jika buku tidak ditemukan
    }

    private void deleteBuku(int bukuId) throws SQLException {
        String query = "DELETE FROM buku WHERE id = ?";
        Connection connection = cConfig.connect;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, bukuId);

        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Buku berhasil dihapus");
        }
    }

    private void clearFields() {
        namaBukuField.clear();
        penulisField.clear();
        tahunTerbitField.clear();
        isbnField.clear();
        nomorRakComboBox.getSelectionModel().clearSelection();
        nomorRakComboBox.setValue(null);
        jumlahField.clear();
        statusComboBox.getSelectionModel().clearSelection();
        statusComboBox.setValue(null);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
