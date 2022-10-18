package me.cjcrafter.webb.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.cjcrafter.webb.ColorWrapper;
import me.cjcrafter.webb.ImageWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    // * ----- IMAGES PANE ----- * //
    @FXML public AnchorPane imagesPane;
    @FXML public Button openFileChooser;
    @FXML public ListView<File> fileList;
    public List<File> currentFiles;


    // * ----- SETTINGS PANE ----- * //
    @FXML public Slider minSpectrumSlider;
    @FXML public Spinner<Integer> minSpectrumSpinner;
    @FXML public ImageView minSpectrumImage;
    public ImageWrapper minSpectrumColor;
    @FXML public Slider maxSpectrumSlider;
    @FXML public Spinner<Integer> maxSpectrumSpinner;
    @FXML public ImageView maxSpectrumImage;
    public ImageWrapper maxSpectrumColor;

    @FXML public Slider brightnessSlider;
    @FXML public Spinner<Integer> brightnessSpinner;
    @FXML public Slider contrastSlider;
    @FXML public Spinner<Integer> contrastSpinner;




    void addImageTabListeners() {
        currentFiles = new ArrayList<>();

        openFileChooser.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Add Images to Process");

            Stage stage = new Stage();
            List<File> files = chooser.showOpenMultipleDialog(stage);
            currentFiles.addAll(files);
            fileList.setItems(FXCollections.observableArrayList(currentFiles));
        });

        imagesPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                currentFiles.addAll(db.getFiles());
                fileList.setItems(FXCollections.observableArrayList(currentFiles));
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        fileList.setItems(FXCollections.observableArrayList(currentFiles));
    }


    void addSettingsTabListeners() {
        minSpectrumColor = new ImageWrapper(16, 16);
        addSpectrumListener(minSpectrumSlider, minSpectrumSpinner, minSpectrumImage, minSpectrumColor);
        maxSpectrumColor = new ImageWrapper(16, 16);
        addSpectrumListener(maxSpectrumSlider, maxSpectrumSpinner, maxSpectrumImage, maxSpectrumColor);
    }

    private void addSpectrumListener(Slider slider, Spinner<Integer> spinner, ImageView image, ImageWrapper wrapper) {
        double wavelength = slider.getValue();
        ColorWrapper color = new ColorWrapper(ColorWrapper.wavelengthToRGB(wavelength));
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(ColorWrapper.SPECTRUM_MIN, ColorWrapper.SPECTRUM_MAX, (int) wavelength));
        image.setImage(wrapper.generateUI());
        wrapper.fill(color);

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            spinner.getValueFactory().setValue(newValue.intValue());
            ColorWrapper newColor = new ColorWrapper(ColorWrapper.wavelengthToRGB(newValue.doubleValue()));
            wrapper.fill(newColor);
        });

        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            slider.setValue(newValue.doubleValue());
            ColorWrapper newColor = new ColorWrapper(ColorWrapper.wavelengthToRGB(newValue.doubleValue()));
            wrapper.fill(newColor);
        });
    }
}
