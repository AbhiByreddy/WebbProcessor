package me.cjcrafter.webb.img;

import me.cjcrafter.webb.ImageWrapper;

import java.util.List;

/**
 * Interface which outlines the process of combining images.
 */
public interface ImageCombiner {

    /**
     * Returns a new {@link ImageWrapper} from the given <code>images</code>.
     * Works by looping through every pixel in the way, and running a
     * calculation to merge the pixels together.
     *
     * @param images The non-null array of images to combine.
     * @return The non-null combined image.
     */
    ImageWrapper combine(ImageWrapper... images);

    default ImageWrapper combine(List<ImageWrapper> images) {
        return combine(images.toArray(new ImageWrapper[0]));
    }
}
