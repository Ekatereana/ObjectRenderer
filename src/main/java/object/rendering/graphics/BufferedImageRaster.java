package object.rendering.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedImageRaster implements Raster {

  private final BufferedImage image;

  public BufferedImageRaster(int width, int height) {

    if (!GraphicsEnvironment.isHeadless()) {
      image = GraphicsEnvironment.getLocalGraphicsEnvironment()
              .getDefaultScreenDevice()
              .getDefaultConfiguration().createCompatibleImage(width, height, Transparency.BITMASK);
    } else {
      image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
  }

  @Override
  public Image toImage() {
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
