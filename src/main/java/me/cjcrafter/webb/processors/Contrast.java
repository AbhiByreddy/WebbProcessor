package me.cjcrafter.webb.processors;

import me.cjcrafter.webb.ColorWrapper;
import me.cjcrafter.webb.ImageWrapper;
import me.cjcrafter.webb.img.ImageUtil;

public class Contrast implements ImageProcessor {

    private final float contrast;

    public Contrast(int contrast) {
        this.contrast = contrast;
    }

    @Override
    public ImageWrapper process(ImageWrapper image) {
        image.request()

        float factor = (259f * (contrast + 255f)) / (255f * (259f - contrast));

        int pixels = image.getWidth() * image.getHeight();
        for (int i = 0; i < pixels; i++) {
            ColorWrapper color = image.getColor(i);
            int r = ImageUtil.clamp((int) (factor * (color.getR() - 128f) + 128f), 0, 255);
            int g = ImageUtil.clamp((int) (factor * (color.getG() - 128f) + 128f), 0, 255);
            int b = ImageUtil.clamp((int) (factor * (color.getB() - 128f) + 128f), 0, 255);
            image.setColor(i, new ColorWrapper(r, g, b));
        }

        image.save()
        return image;
    }
}
