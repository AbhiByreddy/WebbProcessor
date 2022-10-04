package me.cjcrafter.webb.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

// https://www.youtube.com/watch?v=9XJicRt_FaI
public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load the FXML file, which stores the UIs layout/buttons.
        URL interfaceUrl = getClass().getResource("/sample.fxml");
        if (interfaceUrl == null) throw new IllegalArgumentException("Could not find UI fxml file");
        Parent root = FXMLLoader.load(interfaceUrl);

        // Load the style sheet
        URL styleUrl = getClass().getResource("/style.css");
        if (styleUrl == null) throw new IllegalArgumentException("Could not find style sheet");
        String style = styleUrl.toExternalForm();

        primaryStage.setTitle("WebbProcessor");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(style);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
