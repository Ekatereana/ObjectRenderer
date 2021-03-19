package com.company.convectors;

import com.company.abstruct.ImageConvector;
import com.company.pojo.ImageInstance;

import java.io.IOException;

public class BMPConvector extends ImageConvector {

    @Override
    public ImageInstance read(ImageInstance inst) {
        super.read(inst);
        System.out.println("BMP read");
        return null;
    }

    @Override
    public String write(ImageInstance inst) throws IOException {
        super.write(inst);
        return null;
    }
}
