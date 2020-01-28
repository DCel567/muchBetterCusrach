package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class App extends Application {

    private static Scene scene;
    static boolean gotDataFromFile = false;
    static boolean gotDataFromSite = false;

    static String filePath;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("logInScene"));
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void openNewScene(String fxmlName){
        try {
            Scene scene = new Scene(App.loadFXML(fxmlName));
            Stage stage = new Stage();
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws SQLException {
        launch();
    }

}