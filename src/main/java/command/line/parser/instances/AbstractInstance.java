package command.line.parser.instances;

import image.converting.enums.ImageType;
import lombok.Data;
import org.di.framework.annotations.Component;

@Component
@Data
public class AbstractInstance {
    private String sourcePath;
    private String outputPath;
    private ImageType goalFormat;
}
