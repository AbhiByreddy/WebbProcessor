package me.cjcrafter.webb.ui;

import me.cjcrafter.webb.ColorWrapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

public class SpectrumMaker {

    public static void main(String[] args) throws IOException {
        BufferedImage img = new BufferedImage(280, 16, BufferedImage.TYPE_INT_RGB);
        int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        for (int x = 0; x < img.getWidth(); x++) {
            int rgb = ColorWrapper.fromWavelength((float) x / img.getWidth()).getRGB();

            for (int y = 0; y < img.getHeight(); y++) {
                data[y * img.getWidth() + x] = rgb;
            }
        }

        ImageIO.write(img, "png", new File("spectrum.png"));
    }
}
