package cont;

import com.config.cConfig;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class tambahbukucontroller {
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
    private TextField statusField;

    @FXML
    private Button simpanButton;

    @FXML
    private Button batalButton;

    @FXML
    private void initialize() {
        cConfig.connection();
        loadNomorRak();
    }

    @FXML
    private void simpan(ActionEvent event) throws IOException{
        String namaBuku = namaBukuField.getText();
        String penulis = penulisField.getText();
        int tahunTerbit = Integer.parseInt(tahunTerbitField.getText());
        String isbn = isbnField.getText();
        String nomorRak = nomorRakComboBox.getValue();
        int jumlah = Integer.parseInt(jumlahField.getText());

        try {
            int nomorRakId = getNomorRakId(nomorRak);

            String query = "INSERT INTO buku (nama_buku, penulis, tahun_terbit, isbn, nomor_rak_id, jumlah, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, 'tersedia')";

            Connection connection = cConfig.connect;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, namaBuku);
            statement.setString(2, penulis);
            statement.setInt(3, tahunTerbit);
            statement.setString(4, isbn);
            statement.setInt(5, nomorRakId);
            statement.setInt(6, jumlah);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data buku berhasil disimpan ke database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cConfig.disconnect();
        }
    }

    private void loadNomorRak() {
        try {
            String query = "SELECT nomor_rak FROM nomor_rak";
            Connection connection = cConfig.connect;
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nomorRak = resultSet.getString("nomor_rak");
                nomorRakComboBox.getItems().add(nomorRak);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getNomorRakId(String nomorRak) throws SQLException {
        String query = "SELECT id FROM nomor_rak WHERE nomor_rak = ?";
        Connection connection = cConfig.connect;
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, nomorRak);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("id");
        }

        return -1; // Return -1 jika nomor rak tidak ditemukan
    }
}
