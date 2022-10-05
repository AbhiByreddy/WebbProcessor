package me.cjcrafter.webb;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;

public class ImageWrapper {

    private final ColorHolder[] pixels;
    private final int width;
    private final int height;

    public ImageWrapper(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new ColorHolder[width * height];

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new ColorHolder(0);
        }
    }

    public ImageWrapper(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = new ColorHolder[width * height];

        switch (image.getType()) {
            case BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_RGB -> {
                int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
                for (int i = 0; i < data.length; i++)
                    pixels[i] = new ColorHolder(data[i]);
            }
            case BufferedImage.TYPE_USHORT_GRAY, BufferedImage.TYPE_USHORT_555_RGB, BufferedImage.TYPE_USHORT_565_RGB -> {
                short[] data = ((DataBufferUShort) image.getRaster().getDataBuffer()).getData();
                for (int i = 0; i < data.length; i++)
                    pixels[i] = new ColorHolder(Short.toUnsignedInt(data[i]));
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ColorHolder getColor(int x, int y) {
        return pixels[y * width + x];
    }

    public void setColor(int x, int y, ColorHolder color) {
        pixels[y * width + x] = color;
    }

    public void tint(ColorHolder tint) {
        for (ColorHolder pixel : pixels)
            pixel.multiply(tint);
    }

    public BufferedImage generateImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < pixels.length; i++)
            data[i] = pixels[i].getRGB();

        return image;
    }
}
