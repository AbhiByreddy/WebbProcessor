package me.cjcrafter.webb;

import javafx.application.Platform;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class ImageWrapper {

    private PixelBuffer<IntBuffer> pixelBuffer;
    private int[] pixels;
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
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ColorWrapper getColor(int x, int y) {
        return getColor(y * width + x);
    }

    public ColorWrapper getColor(int i) {
        if (i < 0 || i >= pixels.length)
            throw new IndexOutOfBoundsException("For index " + i + " and length " + pixels.length + " (" + width + "x" + height + ")");
        return new ColorWrapper(pixels[i]);
    }

    public void setColor(int x, int y, ColorWrapper color) {
        setColor(y * width + x, color);
        if (pixelBuffer != null) pixelBuffer.updateBuffer(temp -> null); // TODO dirty only region
    }

    public void setColor(int i, ColorWrapper color) {
        if (i < 0 || i >= pixels.length)
            throw new IndexOutOfBoundsException("For index " + i + " and length " + pixels.length + " (" + width + "x" + height + ")");
        pixels[i] = color.getRGB();
        if (pixelBuffer != null) pixelBuffer.updateBuffer(temp -> null); // TODO dirty only region
    }

    public void tint(ColorWrapper tint) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = new ColorWrapper(pixels[i]).multiply(tint).getRGB();
        }
        if (pixelBuffer != null) pixelBuffer.updateBuffer(temp -> null);
    }

    public void fill(ColorWrapper fill) {
        Arrays.fill(pixels, fill.getRGB());
        if (pixelBuffer != null) pixelBuffer.updateBuffer(temp -> null);
    }

    public BufferedImage generateImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(pixels, 0, data, 0, pixels.length);

        return image;
    }

    public javafx.scene.image.Image generateUI() {
        IntBuffer intBuffer = IntBuffer.allocate(width * height);
        pixels = intBuffer.array();
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        pixelBuffer = new PixelBuffer<>(width, height, intBuffer, pixelFormat);
        return new WritableImage(pixelBuffer);
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
