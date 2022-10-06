package me.cjcrafter.webb.img;

import me.cjcrafter.webb.ColorWrapper;
import me.cjcrafter.webb.ImageWrapper;

/**
 * Combines image by simply adding the colors together. If the color
 * value goes beyond white, it is clamped back to white.
 */
public class AdditiveCombiner implements ImageCombiner {

    @Override
    public ImageWrapper combine(ImageWrapper... images) {
        int width = images[0].getWidth();
        int height = images[0].getHeight();
        int pixels = width * height;

        float[] reds = new float[pixels];
        float[] greens = new float[pixels];
        float[] blues = new float[pixels];

        // Loop through the given images and add up their RGB values
        for (ImageWrapper image : images) {

            // Cache the pixels array for performance
            System.out.println("Getting data for image");

            // Check to make sure image sizes match up
            if (image.getWidth() != width || image.getHeight() != height)
                throw new IllegalArgumentException("Images need to be the same size");

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    ColorWrapper sample = image.getColor(x, y);
                    int index = y * width + x;

                    // Additively combine pixels
                    reds[index] += sample.r;
                    greens[index] += sample.g;
                    blues[index] += sample.b;
                }
            }
        }

        // Construct the new image
        ImageWrapper temp = new ImageWrapper(width, height);
        for (int i = 0; i < pixels; i++) {
            temp.setColor(i, new ColorWrapper(reds[i], greens[i], blues[i]));
        }
        return temp;
    }
}
