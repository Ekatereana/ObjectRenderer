package obj.rendering.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.util.Arrays;

public class BufferedImageRaster implements RasterToImageMapper {

    private final BufferedImage image;

    public BufferedImageRaster(int width, int height) {

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    }

    public BufferedImageRaster(int width, int height, Color color) {

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, color.getRGB());
            }
        }
    }

    @Override
    public Image mapToImage() {
        return image;
    }

    @Override
    public void setPixel(int x, int y, int rgb) {
        image.setRGB(x, y, rgb);
    }

    @Override
    public void setPixel(int x, int y, byte red, byte green, byte blue) {
        image.setRGB(x, y, 0xFF << 24 | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF));
    }

    @Override
    public Color getPixelColor(int x, int y) {
        return new Color(image.getRGB(x, y));
    }

    @Override
    public int getPixelRGB(int x, int y) {
        return image.getRGB(x, y);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}
