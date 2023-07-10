package cont;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import com.config.cConfig;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardintController implements Initializable {

    @FXML
    private Text peminjam;

    @FXML
    private Text keterlambatan;

    @FXML
    private Text anggota;

    @FXML
    private Text buku;

    @FXML
    private BarChart<String, Number> chart;

    @FXML
    private CategoryAxis xAxis;

    private Connection connection; // Objek koneksi ke database

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inisialisasi controller
        // ...

        // Membuat koneksi ke database
        cConfig.connection();
        connection = cConfig.connect;
        
        if (connection != null) {
            System.out.println("Koneksi berhasil");
            updateChart();
            updatePeminjam();
            updateKeterlambatan();
            updateAnggota();
            updateBuku();
        } else {
            System.out.println("Koneksi gagal");
            // Menampilkan pesan error jika gagal terkoneksi ke database
            showAlert("Failed to connect to database!");
        }
        
    }

    public void updateChart() {
        // Mendapatkan tanggal 20 hari kebelakang dari tanggal sistem operasi
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(19);
    
        // Format tanggal ke dalam format yang sesuai dengan database
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);
    
        // Query untuk mengambil data peminjaman buku dalam periode 20 hari terakhir
        String query = "SELECT tanggal_peminjaman, COUNT(*) as jumlah_peminjaman FROM peminjaman WHERE tanggal_peminjaman BETWEEN ? AND ? GROUP BY tanggal_peminjaman";
    
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, startDateStr);
            statement.setString(2, endDateStr);
            ResultSet resultSet = statement.executeQuery();
    
            // Membersihkan data sebelumnya dari chart
            chart.getData().clear();
    
            XYChart.Series<String, Number> series = new XYChart.Series<>();
    
            while (resultSet.next()) {
                String tanggalPeminjaman = resultSet.getString("tanggal_peminjaman");
                int jumlahPeminjaman = resultSet.getInt("jumlah_peminjaman");
                series.getData().add(new XYChart.Data<>(tanggalPeminjaman, jumlahPeminjaman));
            }
    
            // Menambahkan series ke chart
            chart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
            // Menampilkan pesan error jika terjadi kesalahan dalam pengambilan data
            showAlert("Failed to fetch data from database!");
        }
    }
    
    public void updatePeminjam() {
        // Query untuk mengambil total jumlah peminjam
        String query = "SELECT COUNT(*) as total_peminjam FROM peminjaman";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int totalPeminjam = resultSet.getInt("total_peminjam");
                peminjam.setText(Integer.toString(totalPeminjam));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Menampilkan pesan error jika terjadi kesalahan dalam pengambilan data
            showAlert("Failed to fetch data from database!");
        }
    }

    public void updateKeterlambatan() {
        // Mendapatkan tanggal saat ini
        LocalDate currentDate = LocalDate.now();

        // Format tanggal ke dalam format yang sesuai dengan database
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDateString = currentDate.format(formatter);

        // Query untuk mengambil jumlah peminjam yang terlambat
        String query = "SELECT COUNT(*) as total_keterlambatan FROM peminjaman WHERE tanggal_pengembalian < ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, currentDateString);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int totalKeterlambatan = resultSet.getInt("total_keterlambatan");
                keterlambatan.setText(Integer.toString(totalKeterlambatan));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Menampilkan pesan error jika terjadi kesalahan dalam pengambilan data
            showAlert("Failed to fetch data from database!");
        }
    }

    public void updateAnggota() {
        // Query untuk mengambil jumlah anggota
        String query = "SELECT COUNT(*) as total_anggota FROM anggota";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int totalAnggota = resultSet.getInt("total_anggota");
                anggota.setText(Integer.toString(totalAnggota));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Menampilkan pesan error jika terjadi kesalahan dalam pengambilan data
            showAlert("Failed to fetch data from database!");
        }
    }

    public void updateBuku() {
        // Query untuk mengambil jumlah buku
        String query = "SELECT COUNT(*) as total_buku FROM buku";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int totalBuku = resultSet.getInt("total_buku");
                buku.setText(Integer.toString(totalBuku));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Menampilkan pesan error jika terjadi kesalahan dalam pengambilan data
            showAlert("Failed to fetch data from database!");
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Metode showAlert() dan metode lainnya
    // ...
}
