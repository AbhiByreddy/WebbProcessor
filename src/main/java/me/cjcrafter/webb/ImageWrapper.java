package me.cjcrafter.webb;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;

public class ImageWrapper {

    private final int[] pixels;
    private final int width;
    private final int height;

    public ImageWrapper(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public ImageWrapper(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = new int[width * height];

        for (int i = 0; i < pixels.length; i++) {
            this.pixels[i] = image.getRGB(i % width, i / width);
        }
        /*
        switch (image.getType()) {
            case BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_RGB -> {
                int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
                for (int i = 0; i < data.length; i++) {
                    pixels[i] = data[i] & 0xffffff;
                    if (pixels[i] < 0)
                        System.out.println("Negative pixel: " + pixels[i]);
                }
            }
            case BufferedImage.TYPE_USHORT_GRAY, BufferedImage.TYPE_USHORT_555_RGB, BufferedImage.TYPE_USHORT_565_RGB -> {
                short[] data = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();
                for (int i = 0; i < data.length; i++) {
                    pixels[i] = Short.toUnsignedInt(data[i]);
                    if (pixels[i] < 0)
                        System.out.println("Negative pixel: " + pixels[i]);
                }
            }
        }
         */
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ColorWrapper getColor(int x, int y) {
        return new ColorWrapper(pixels[y * width + x]);
    }

    public void setColor(int x, int y, ColorWrapper color) {
        pixels[y * width + x] = color.getRGB();
    }

    public void tint(ColorWrapper tint) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new ColorWrapper(pixels[i]).multiply(tint).getRGB();
        }
    }

    public BufferedImage generateImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(pixels, 0, data, 0, pixels.length);

        return image;
    }

    public ImageWrapper resize(int width, int height) {
        Image resized = generateImage().getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gfx = image.getGraphics();
        gfx.drawImage(resized, 0, 0, null);
        gfx.dispose();
        return new ImageWrapper(image);
    }
}
