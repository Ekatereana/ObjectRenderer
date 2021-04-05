package image.converting;

import image.converting.abstruct.ImageConvector;
import image.converting.convectors.BMPConvector;
import image.converting.convectors.PNGConvector;
import image.converting.convectors.PPMConvector;
import image.converting.enums.ImageType;
import image.converting.pojo.ImageInstance;
import image.converting.pojo.ImageMappingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        HashMap<ImageType, Supplier<ImageConvector>> factory = new HashMap<>();
        factory.put(ImageType.PPM, () -> new PPMConvector());
        factory.put(ImageType.BMP, () -> new BMPConvector());
        factory.put(ImageType.PNG, () -> new PNGConvector());
        factory.put(ImageType.UNKNOWN, () -> null);
        CommandLineParser parser = new CommandLineParser();
        long start;
        long end;

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

