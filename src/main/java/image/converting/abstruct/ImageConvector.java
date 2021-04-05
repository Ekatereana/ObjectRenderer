package image.converting.abstruct;

import image.converting.interfaces.ImageMapper;
import image.converting.pojo.ImageInstance;
import image.converting.pojo.ImageMappingException;

import java.io.IOException;

public abstract class ImageConvector implements ImageMapper {


    @Override
    public ImageInstance read(ImageInstance inst) {
        return null;
    }
    @Override
    public String write(ImageInstance inst) throws IOException, ImageMappingException {
        return null;
    }
}
