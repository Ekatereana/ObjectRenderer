package image.converting.interfaces;

import image.converting.pojo.ImageInstance;
import image.converting.pojo.ImageMappingException;

import java.io.IOException;
import java.io.InputStream;

public interface ImageMapper {
    ImageInstance read(ImageInstance inst);
//    reader helpers
    Object readHeader(InputStream is, String fileName) throws ImageMappingException, IOException;

    String write(ImageInstance inst) throws IOException, ImageMappingException;
}
