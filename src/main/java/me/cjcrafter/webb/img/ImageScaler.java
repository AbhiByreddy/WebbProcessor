package me.cjcrafter.webb.img;

import me.cjcrafter.webb.ImageWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Helper class to scale a collection of images to the same size.
 */
public class ImageScaler {

    private final List<ImageWrapper> images;
    public ImageScaler() {
        this.images = new ArrayList<>();
    }

    /**
     * Adds the given images to the scaler.
     *
     * @param images The array of images.
     * @return The non-null reference to this (builder pattern).
     */
    public ImageScaler addImages(ImageWrapper... images) {
        this.images.addAll(Arrays.asList(images));
        return this;
    }

    /**
     * Adds the given images to the scaler.
     *
     * @param images The collection of images.
     * @return The non-null reference to this (builder pattern).
     */
    public ImageScaler addImages(List<ImageWrapper> images) {
        this.images.addAll(images);
        return this;
    }

    public List<ImageWrapper> getScaled() {
        // First find the largest dimension that we should scale up to. It is
        // important that we use the largest image, so we don't lose any data.
        int width = images.stream().max(Comparator.comparingInt(ImageWrapper::getWidth)).orElseThrow().getWidth();
        int height = images.stream().max(Comparator.comparingInt(ImageWrapper::getHeight)).orElseThrow().getHeight();

        // Scale the image, then convert it back to a buffered image.
        return images.stream().map(img -> img.resize(width, height)).toList();
    }
}
