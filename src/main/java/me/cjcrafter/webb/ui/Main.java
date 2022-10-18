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
import me.cjcrafter.webb.ColorWrapper;
import me.cjcrafter.webb.ImageWrapper;
import me.cjcrafter.webb.img.AdditiveCombiner;
import me.cjcrafter.webb.img.ImageScaler;
import me.cjcrafter.webb.processors.Brightness;
import me.cjcrafter.webb.processors.Colorizer;
import me.cjcrafter.webb.processors.Contrast;
import me.cjcrafter.webb.processors.StarCoreFixer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        controller.addImageTabListeners();
        controller.addSettingsTabListeners();

        controller.imagesPane.setOnDragOver(event -> {
            if (event.getGestureSource() != controller.imagesPane && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        controller.imagesPane.setOnDragDropped(event -> {
            try {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    List<String> names = new ArrayList<>();
                    List<ImageWrapper> images = db.getFiles().stream().map(file -> {
                        try {
                            names.add(file.getName());
                            return ImageIO.read(file);
                        } catch (IOException ex) {
                            throw new InternalError(ex);
                        }
                    }).map(ImageWrapper::new).toList();

                    File file = new File("ScaledImages");
                    file.mkdirs();
                    int fileNumber = file.listFiles() == null ? 0 : file.listFiles().length;

                    images = new ArrayList<>(images);

                    // NIRCAM can see from 0.6 micrometers to 4.8 micrometers.
                    // The names of the images come with this information, we
                    // just need to convert it to nanometers
                    // https://jwst-docs.stsci.edu/jwst-near-infrared-camera/nircam-instrumentation/nircam-filters
                    int[] wavelengths = names.stream()
                            .peek(System.out::println)
                            .map(str -> Pattern.compile("f\\d\\d\\d([nmw])_").matcher(str))
                            .peek(matcher -> System.out.println("Match: " + matcher.find()))
                            .map(Matcher::group)
                            .map(str -> str.substring(1, str.length() - 2))
                            .mapToInt(str -> 10 * Integer.parseInt(str))
                            .toArray();

                    float min = Arrays.stream(wavelengths).min().orElseThrow();
                    float max = Arrays.stream(wavelengths).max().orElseThrow();

                    List<ColorWrapper> colors = Arrays.stream(wavelengths).mapToObj(i -> {
                        float percentage = (i - min) / (max - min);
                        float adjusted = 450 + percentage * (650 - 450);
                        ColorWrapper wrapper = ColorWrapper.fromWavelength(percentage);
                        System.out.println("Got " + percentage + " (" + (int) adjusted+ ") from ilerp(" + i + ", " + min + ", " + max + "). This is " + wrapper);
                        return wrapper;
                    }).toList();

                    // Scale the images
                    System.out.println("Fixing star cores");
                    images.forEach(img -> new StarCoreFixer().process(img));

                    // Fixing brightness and contrast
                    images.forEach(img -> new Brightness(-3).process(img));
                    images.forEach(img -> new Contrast(3).process(img));

                    System.out.println("Applying Color");
                    for (int i = 0; i < images.size(); i++) {
                        System.out.println("Coloring image " + i);
                        images.set(i, new Colorizer(colors.get(i)).process(images.get(i)));

                        //File output = new File(file, "Image" + fileNumber + "_" + new String[]{ "red", "green", "blue"}[i] + ".png");
                        //ImageIO.write(images.get(i).generateImage(), "png", output);
                    }
                    System.out.println("Scaling images");
                    images = new ImageScaler().addImages(images).getScaled();
                    images.forEach(image -> {
                        File output = new File(file, UUID.randomUUID() + ".png");
                        try {
                            ImageIO.write(image.generateImage(), "png", output);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    ImageWrapper image = new AdditiveCombiner().combine(images);
                    try {
                        File output = new File(file, "Image" + fileNumber + ".png");
                        ImageIO.write(image.generateImage(), "png", output);
                        System.out.println("DONE! Wrote to " + output);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
