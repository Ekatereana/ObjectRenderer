package com.company;

import com.company.abstruct.ImageConvector;
import com.company.convectors.BMPConvector;
import com.company.convectors.PPMConvector;
import com.company.enums.ImageType;
import com.company.pojo.ImageInstance;

import java.util.HashMap;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        HashMap<ImageType, Supplier<ImageConvector>> factory = new HashMap<>();
        factory.put(ImageType.PPM, () -> new PPMConvector());
        factory.put(ImageType.BMP, () -> new BMPConvector());
        CommandLineParser parser = new CommandLineParser();
        try{
            ImageInstance ii = parser.parseCommandLineArgs(args);
            ImageConvector from = factory.get(ii.getSourceFormat()).get();
            ImageConvector to = factory.get(ii.getGoalFormat()).get();
            to.write(from.read(ii));

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Repeat please");


        }

    }
}

