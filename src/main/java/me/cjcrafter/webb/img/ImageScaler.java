package me.cjcrafter.webb.img;

import com.sun.javafx.scene.traversal.Algorithm;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Helper class to scale a collection of images to the same size.
 */
public class ImageScaler {

    private final List<BufferedImage> images;
    private final Algorithm algorithm;

    public ImageScaler(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.images = new ArrayList<>();
    }

    /**
     * Adds the given images to the scaler.
     *
     * @param images The array of images.
     * @return The non-null reference to this (builder pattern).
     */
    public ImageScaler addImages(BufferedImage... images) {
        this.images.addAll(Arrays.asList(images));
        return this;
    }

    /**
     * Adds the given images to the scaler.
     *
     * @param images The collection of images.
     * @return The non-null reference to this (builder pattern).
     */
    public ImageScaler addImages(List<BufferedImage> images) {
        this.images.addAll(images);
        return this;
    }

    public List<BufferedImage> getScaled() {
        // First find the largest dimension that we should scale up to. It is
        // important that we use the largest image, so we don't lose any data.
        int width = images.stream().max(Comparator.comparingInt(BufferedImage::getWidth)).orElseThrow().getWidth();
        int height = images.stream().max(Comparator.comparingInt(BufferedImage::getHeight)).orElseThrow().getHeight();

        // Scale the image, then convert it back to a buffered image.
        return images.stream().map(img -> img.getScaledInstance(width, height, algorithm.scaleMode))
                .map(img -> {
                    BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    Graphics gfx = temp.getGraphics();
                    gfx.drawImage(img, 0, 0, null);
                    gfx.dispose();
                    return temp;
                }).toList();
    }


    public enum Algorithm {
        FAST(BufferedImage.SCALE_FAST),
        SMOOTH(BufferedImage.SCALE_SMOOTH);

        private final int scaleMode;

        Algorithm(int scaleMode) {
            this.scaleMode = scaleMode;
        }

        public int getScaleMode() {
            return scaleMode;
        }
    }
}
