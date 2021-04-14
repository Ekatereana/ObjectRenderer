package services;

import image.converting.pojo.ColorSpace;

import java.io.IOException;

public interface RenderService {
    ColorSpace render(String path) throws IOException;
}
