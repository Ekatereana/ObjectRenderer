package com.company.convectors;

import com.company.abstruct.ImageConvector;
import com.company.pojo.ImageInstance;

public class BMPConvector extends ImageConvector {

    @Override
    public ImageInstance read(ImageInstance inst) {
        super.read(inst);
        System.out.println("BMP read");
        return null;
    }

    @Override
    public ImageInstance write(ImageInstance inst) {
        super.write(inst);
        return null;
    }
}
