package me.cjcrafter.webb.processors;

import me.cjcrafter.webb.ColorWrapper;
import me.cjcrafter.webb.ImageWrapper;

public class Colorizer implements ImageProcessor {

    private final ColorWrapper tint;

    public Colorizer(ColorWrapper tint) {
        this.tint = tint;
    }

    @Override
    public ImageWrapper process(ImageWrapper image) {
        image.tint(tint);
        return image;
    }
}
