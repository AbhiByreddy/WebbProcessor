package me.cjcrafter.webb.processors;

import me.cjcrafter.webb.img.ImageUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferUShort;

/**
 * For some reason, star cores end up black (more often when the star is larger
 * or brighter). To remedy this, we loop through every pixel and check if their
 * grayscale color is less than a threshold. When this happens, we fill the pixel
 * in with the average of the neighboring pixels.
 *
 * <p>This fix sounds like "cheating", but it was suggested by multiple NASA
 * scientists so I accept it.
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
    public void process(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        short[] data = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                float color = ((int) data[y * width + x]) / 255f;

                if (color > threshold)
                    continue;

                int total = 0;
                int count = 0;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        short sample = data[(y + dy) * width + (x + dx)];

                        if (sample > (threshold * 255f)) {
                            total += sample;
                            count++;
                        }
                    }
                }

                if (count > 0) {
                    total /= count;
                    data[y * width + x] = (short) total;
                } else {
                    data[y * width + x] = (short) ImageUtil.clamp((int) (threshold * 255f), 0, 255);
                }
            }
        }
    }
}
