import com.config.cConfig;
import cont.loginpagecontroller;

import java.io.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class iPerpus extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginpage.fxml"));
    Parent root = loader.load();

    loginpagecontroller controller = loader.getController();
    controller.setStage(primaryStage);

    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.show();
}   
    
    public static void main(String[] args) throws Exception {
        cConfig.connection();
        launch(args);
    }
}
