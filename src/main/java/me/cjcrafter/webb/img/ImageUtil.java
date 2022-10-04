package me.cjcrafter.webb.img;

/**
 * Utility class containing static methods for images.
 */
public final class ImageUtil {

    /**
     * Since this class is just for static methods, don't let anyone
     * instantiate this class.
     */
    private ImageUtil() {
    }

    /**
     * Clamps the given float 0..1
     *
     * @param value The value to clamp.
     * @return The clamped value.
     */
    public static float clamp01(float value) {
        return Math.min(Math.max(0f, value), 1f);
    }

    /**
     * Clamps the given int min..max
     *
     * @param value The value to clamp.
     * @param min Minimum value.
     * @param max Maximum value.
     * @return The clamped value.
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Returns a <code>float[3]</code> with the RGB components of the given
     * integer. The alpha channel is ignored. The floats will be clamped 0..1.
     *
     * @param rgb The red, green, and blue channels from a pixel.
     * @return The 3 float values.
     */
    public static float[] fromInt(int rgb) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;

        // Convert from range 0..255 to 0..1
        return new float[] { r / 255f, g / 255f, b / 255f };
    }

    /**
     * Returns the combined RGB channels in an integer.
     *
     * @param r The 0..1 red component.
     * @param g The 0..1 blue component.
     * @param b The 0..1 green component.
     * @return The combined color.
     */
    public static int toInt(float r, float g, float b) {
        int tempR = clamp((int) (r * 255f), 0, 255);
        int tempG = clamp((int) (g * 255f), 0, 255);
        int tempB = clamp((int) (b * 255f), 0, 255);
        return (tempR << 16) | (tempG << 8) | tempB;
    }
}
