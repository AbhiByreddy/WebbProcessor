package me.cjcrafter.webb.processors;

import me.cjcrafter.webb.ColorWrapper;
import me.cjcrafter.webb.ImageWrapper;

public class Brightness implements ImageProcessor {

    private final float brightness;

    public Brightness(int brightness) {
        this.brightness = brightness / 255f;
    }

    @Override
    public ImageWrapper process(ImageWrapper image) {
        int pixels = image.getWidth() * image.getHeight();
        for (int i = 0; i < pixels; i++) {
            ColorWrapper color = image.getColor(i);
            color.r += brightness;
            color.g += brightness;
            color.b += brightness;
            color.truncate();

            image.setColor(i, color);
        }

        return image;
    }
}
