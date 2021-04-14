package command.line.parser.instances;

import image.converting.pojo.ColorSpace;
import image.converting.pojo.headers.Header;
import image.converting.enums.ImageType;
import lombok.Data;

import java.io.InputStream;

@Data
public class ImageInstance extends AbstractInstance {
    private ImageType sourceFormat;
    private InputStream is;
    private ColorSpace rgb;
    private Header header;
}
