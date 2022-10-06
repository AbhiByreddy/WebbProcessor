package me.cjcrafter.webb;

import me.cjcrafter.webb.img.ImageUtil;

import java.awt.*;

public class ColorWrapper implements Cloneable {

    public float r;
    public float g;
    public float b;

    public ColorWrapper(int rgb) {
        this((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
    }

    public ColorWrapper(Color color) {
        r = color.getRed() / 255f;
        g = color.getGreen() / 255f;
        b = color.getBlue() / 255f;
    }

    public ColorWrapper(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public ColorWrapper(int r, int g, int b) {
        this.r = r / 255f;
        this.g = g / 255f;
        this.b = b / 255f;
    }

    public ColorWrapper add(ColorWrapper other) {
        this.r += other.r;
        this.g += other.g;
        this.b += other.b;
        return this;
    }

    public ColorWrapper multiply(ColorWrapper other) {
        this.r *= other.r;
        this.g *= other.g;
        this.b *= other.b;
        return this;
    }

    public int getR() {
        return ImageUtil.clamp((int) (r * 255f), 0, 255);
    }

    public int getG() {
        return ImageUtil.clamp((int) (g * 255f), 0, 255);
    }

    public int getB() {
        return ImageUtil.clamp((int) (b * 255f), 0, 255);
    }

    public int getRGB() {
        return getR() << 16 | getG() << 8 | getB();
    }

    @Override
    public ColorWrapper clone() {
        try {
            return (ColorWrapper) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }
}
