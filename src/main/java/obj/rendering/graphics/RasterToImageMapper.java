package obj.rendering.graphics;

import image.converting.pojo.ImageInstance;
import java.awt.*;

public interface RasterToImageMapper {

    Image mapToImage();

    void setPixel(int x, int y, int rgb);
    void setPixel(int x, int y, byte red, byte green, byte blue);

    Color getPixelColor(int x, int y);
    int getPixelRGB(int x, int y);

    int getWidth();
    int getHeight();



}
