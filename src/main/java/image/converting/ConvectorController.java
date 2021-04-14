package image.converting;

import image.converting.abstruct.ImageConvector;
import image.converting.convectors.BMPConvector;
import image.converting.convectors.PNGConvector;
import image.converting.convectors.PPMConvector;
import image.converting.enums.ImageType;
import command.line.parser.instances.ImageInstance;
import image.converting.pojo.ImageMappingException;
import org.di.framework.annotations.Component;
import services.ConvecterService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class ConvectorController implements ConvecterService {
    private static Map<ImageType, Supplier<ImageConvector>> factory;

    static {
        factory = new HashMap<>();
        factory.put(ImageType.PPM, () -> new PPMConvector());
        factory.put(ImageType.BMP, () -> new BMPConvector());
        factory.put(ImageType.PNG, () -> new PNGConvector());
        factory.put(ImageType.UNKNOWN, () -> null);
    }


    @Override
    public void convertTo(ImageInstance ii){
        try {
            if (ii.getGoalFormat().equals(ImageType.UNKNOWN)) {
                throw new ImageMappingException("Unknown file format", ii.getSourcePath());
            }
            ImageConvector to = factory.get(ii.getGoalFormat()).get();
            to.write(ii);
        } catch (NullPointerException | IOException | ImageMappingException e) {
            e.printStackTrace();
            System.out.println("Try again please");
        }
    }

    @Override
    public void convertFrom(ImageInstance ii){
        try {
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
