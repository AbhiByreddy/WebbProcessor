package me.cjcrafter.webb.ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import me.cjcrafter.webb.img.AdditiveCombiner;
import me.cjcrafter.webb.img.ImageScaler;
import me.cjcrafter.webb.processors.StarCoreFixer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

// https://www.youtube.com/watch?v=9XJicRt_FaI
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load the FXML file, which stores the UIs layout/buttons.
        URL interfaceUrl = getClass().getResource("/sample.fxml");
        if (interfaceUrl == null) throw new IllegalArgumentException("Could not find UI fxml file");
        FXMLLoader loader = new FXMLLoader(interfaceUrl);
        Parent root = loader.load();

        // Load the style sheet
        URL styleUrl = getClass().getResource("/style.css");
        if (styleUrl == null) throw new IllegalArgumentException("Could not find style sheet");
        String style = styleUrl.toExternalForm();

        primaryStage.setTitle("WebbProcessor");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(style);

        primaryStage.setScene(scene);
        primaryStage.show();

        Controller controller = loader.getController();
        controller.combineImages.setOnDragOver(event -> {
            if (event.getGestureSource() != controller.combineImages && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        controller.combineImages.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                List<BufferedImage> images = db.getFiles().stream().map(file -> {
                    try {
                        return ImageIO.read(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }).toList();

                // Scale the images
                images.forEach(img -> new StarCoreFixer().process(img));
                images = new ImageScaler(ImageScaler.Algorithm.SMOOTH).addImages(images).getScaled();

                BufferedImage image = new AdditiveCombiner().combine(images.toArray(new BufferedImage[0]));
                try {
                    ImageIO.write(image, "jpg", new File(UUID.randomUUID() + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //combineImages.setText(db.getFiles().toString());
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
