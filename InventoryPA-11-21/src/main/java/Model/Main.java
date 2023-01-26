package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
       Parent root = FXMLLoader.load(getClass().getResource("/View/MainView2.fxml"));
       primaryStage.setTitle("Main");
       primaryStage.setScene(new Scene(root, 1021, 363));
       primaryStage.show();
    }



    public static void main(String[] args) {
        TestParts.addTestData();
        launch();
    }
}