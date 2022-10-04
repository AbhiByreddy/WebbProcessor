package me.cjcrafter.webb.img;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Combines image by simply adding the colors together. If the color
 * value goes beyond white, it is clamped back to white.
 */
public class AdditiveCombiner implements ImageCombiner {

    @Override
    public BufferedImage combine(BufferedImage... images) {
        int width = images[0].getWidth();
        int height = images[0].getHeight();
        int pixels = width * height;

        float[] reds = new float[pixels];
        float[] greens = new float[pixels];
        float[] blues = new float[pixels];

        // Loop through the given images and add up their RGB values
        for (BufferedImage image : images) {

            // Check to make sure image sizes match up
            if (image.getWidth() != width || image.getHeight() != height)
                throw new IllegalArgumentException("Images need to be the same size");

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    float[] converted = ImageUtil.fromInt(image.getRGB(x, y));
                    int index = y * width + x;

                    // Additively combine pixels
                    reds[index] += converted[0];
                    greens[index] += converted[1];
                    blues[index] += converted[2];
                }
            }
        }

        // Construct the new image
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] raw = ((DataBufferInt)temp.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < raw.length; i++) {
            raw[i] = ImageUtil.toInt(ImageUtil.clamp01(reds[i]), ImageUtil.clamp01(greens[i]), ImageUtil.clamp01(blues[i]));
        }

        return temp;
    }
}
