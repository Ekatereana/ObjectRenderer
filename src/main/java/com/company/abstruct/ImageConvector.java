package com.company.abstruct;

import com.company.interfaces.ImageMapper;
import com.company.pojo.ImageInstance;

public abstract class ImageConvector implements ImageMapper {


    @Override
    public ImageInstance read(ImageInstance inst) {
        return null;
    }
    @Override
    public ImageInstance write(ImageInstance inst) {
        return null;
    }
}
