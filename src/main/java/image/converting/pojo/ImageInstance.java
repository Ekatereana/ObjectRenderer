package image.converting.pojo;

import image.converting.pojo.headers.Header;
import image.converting.enums.ImageType;
import lombok.Data;

import java.io.InputStream;

@Data
public class ImageInstance {
    private ImageType sourceFormat;
    private String sourcePath;
    private String outputPath;
    private ImageType goalFormat;
    private InputStream is;
    private ColorSpace rgb;
    private Header header;
}
