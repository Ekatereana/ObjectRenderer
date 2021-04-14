package object.rendering.graphics;

import java.awt.*;

public interface Raster {

  Image toImage();

  void setPixel(int x, int y, int rgb);
  void setPixel(int x, int y, byte red, byte green, byte blue);

  Color getPixelColor(int x, int y);
  int getPixelRGB(int x, int y);

  int getWidth();
  int getHeight();

}
