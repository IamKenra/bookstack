package cont;

import com.config.cConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class tambahbukucontroller {

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField jumlahBukuField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField deskripsiField;

    private Connection connection;

    public tambahbukucontroller() {
        connection = cConfig.connect;
    }

    @FXML
    private void addBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        int year = Integer.parseInt(yearField.getText());
        int jumlahBuku = Integer.parseInt(jumlahBukuField.getText());
        String isbn = isbnField.getText();
        String deskripsi = deskripsiField.getText();

        String query = "INSERT INTO books (title, author, year, jumlah_buku, isbn, deskripsi) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setInt(3, year);
            statement.setInt(4, jumlahBuku);
            statement.setString(5, isbn);
            statement.setString(6, deskripsi);

            statement.executeUpdate();
            System.out.println("Buku berhasil ditambahkan ke dalam database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
