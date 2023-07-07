package cont;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class bukucontroller implements Initializable {

        @FXML
    private Label titleLabel;
    
    @FXML
    private Label authorLabel;
    
    @FXML
    private Label publisherLabel;
    
    @FXML
    private Label isbnLabel;
    
    @FXML
    private Label shelfNumberLabel;
    
    @FXML
    private Label bookCountLabel;
    
    public void initialize() {
        Book book = getBookFromDataSource();
        
        titleLabel.setText("Judul: " + book.getTitle());
        authorLabel.setText("Penulis: " + book.getAuthor());
        publisherLabel.setText("Penerbit: " + book.getPublisher());
        isbnLabel.setText("ISBN: " + book.getIsbn());
        shelfNumberLabel.setText("No. Rak: " + book.getShelfNumber());
        bookCountLabel.setText("Jumlah Buku: " + book.getBookCount());
    }
    
    private Book getBookFromDataSource() {
        return new Book("Judul Buku", "Penulis Buku", "Penerbit Buku", "ISBN Buku", "No. Rak", 10);
    }
}




