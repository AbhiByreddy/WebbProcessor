package me.cjcrafter.webb.processors;

import me.cjcrafter.webb.ColorWrapper;
import me.cjcrafter.webb.ImageWrapper;

/**
 * For some reason, star cores end up black (more often when the star is larger
 * or brighter). To remedy this, we loop through every pixel and check if their
 * grayscale color is less than a threshold. When this happens, we fill the pixel
 * in with the average of the neighboring pixels.
 *
 * <p>This fix sounds like "cheating", but it was suggested by multiple NASA
 * scientists... so I accept it.
 */
public class StarCoreFixer implements ImageProcessor {

    private final float threshold;

    public StarCoreFixer() {
        this(1f / 255f);
    }

    public StarCoreFixer(float threshold) {
        this.threshold = threshold;
    }

    @Override
    public ImageWrapper process(ImageWrapper image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                float color = image.getColor(x, y).r;

                // No need to fix the pixels since they aren't black
                if (color > threshold)
                    continue;

                float total = 0;
                int count = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        float sample = image.getColor(x + dx, y + dy).r;

                        if (sample > threshold) {
                            total += sample;
                            count++;
                        }
                    }
                }

                if (count > 0) {
                    total /= count;
                    image.setColor(x, y, new ColorWrapper(total, total, total));
                } else {
                    image.setColor(x, y, new ColorWrapper(threshold, threshold, threshold));
                }
            }
        }

        return image;
    }
}
