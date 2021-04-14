package image.converting;

import command.line.parser.CommandLineParser;
import image.converting.abstruct.ImageConvector;
import image.converting.convectors.BMPConvector;
import image.converting.convectors.PNGConvector;
import image.converting.convectors.PPMConvector;
import image.converting.enums.ImageType;
import image.converting.pojo.ImageInstance;
import image.converting.pojo.ImageMappingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConvectorController {
    private static Map<ImageType, Supplier<ImageConvector>> factory;
    private CommandLineParser parser;

    static {
        factory = new HashMap<>();
        factory.put(ImageType.PPM, () -> new PPMConvector());
        factory.put(ImageType.BMP, () -> new BMPConvector());
        factory.put(ImageType.PNG, () -> new PNGConvector());
        factory.put(ImageType.UNKNOWN, () -> null);
    }

    public void run(String[] args){
        try {
            ImageInstance ii = parser.parseCommandLineArgs(args);
            if (ii.getGoalFormat().equals(ImageType.UNKNOWN)) {
                throw new ImageMappingException("Unknown file format", ii.getSourcePath());
            }
            ImageConvector from = factory.get(ii.getSourceFormat()).get();
            ImageConvector to = factory.get(ii.getGoalFormat()).get();
            to.write(from.read(ii));
        } catch (NullPointerException | IOException | ImageMappingException e) {
            e.printStackTrace();
            System.out.println("Try again please");
        }
    }
}
