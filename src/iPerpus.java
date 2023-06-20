import com.config.cConfig;
import cont.loginpagecontroller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class iPerpus extends Application {

    @Override
    public void start(Stage primaryStage) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loginpage.fxml"));
        Parent root = loader.load();

        loginpagecontroller controller = loader.getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        primaryStage.show();
        System.out.println("Berhasil meload loginpage");
    } catch (IOException e) {
        e.printStackTrace();
    }
}   

    public void close(Stage primaryStage){
        primaryStage.close();
    }

    public static void main(String[] args) {
        cConfig.connection();
        launch(args);
    }
}
