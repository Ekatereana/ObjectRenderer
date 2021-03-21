package com.company;

import com.company.abstruct.ImageConvector;
import com.company.convectors.BMPConvector;
import com.company.convectors.PNGConvector;
import com.company.convectors.PPMConvector;
import com.company.enums.ImageType;
import com.company.pojo.ImageInstance;
import com.company.pojo.ImageMappingException;

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

