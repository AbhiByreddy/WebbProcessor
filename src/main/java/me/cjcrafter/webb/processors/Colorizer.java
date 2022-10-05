package me.cjcrafter.webb.processors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;

public class Colorizer implements ImageProcessor {

    private final int rgb;

    public Colorizer(Color color) {
        this.rgb = color.getRGB();
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        short[] from = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();
        int[] to = ((DataBufferInt) temp.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < to.length; i++) {
            int sample = from[i];
            float percentage = sample / 255f;
            to[i] = (int) (percentage * rgb);
        }

        return temp;
    }
}
