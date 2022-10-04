package me.cjcrafter.webb.img;

import java.awt.image.BufferedImage;

/**
 * Interface which outlines the process of combining images.
 */
public interface ImageCombiner {

    /**
     * Returns a new {@link BufferedImage} from the given <code>images</code>.
     * Works by looping through every pixel in the way, and running a
     * calculation to merge the pixels together.
     *
     * @param images The non-null array of images to combine.
     * @return The non-null combined image.
     */
    BufferedImage combine(BufferedImage... images);
}
