package me.cjcrafter.webb.processors;

import me.cjcrafter.webb.img.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;

public class Colorizer implements ImageProcessor {

    private final int rgb;

    public Colorizer(Color color) {
        this.rgb = (color.getRed() << 16) | (color.getGreen() << 8) | color.getBlue();
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        short[] from = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();
        int[] to = ((DataBufferInt) temp.getRaster().getDataBuffer()).getData();

        try {
            for (int i = 0; i < to.length; i++) {
                float sample = (((int) from[i]) & 0xff) / 255f;
                int color = ImageUtil.lerp(0, rgb, sample);
                to[i] = color;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return temp;
    }
}
