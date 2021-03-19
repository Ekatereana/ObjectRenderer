package com.company.abstruct;

import com.company.interfaces.ImageMapper;
import com.company.pojo.ImageInstance;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class ImageConvector implements ImageMapper {


    @Override
    public ImageInstance read(ImageInstance inst) {
        return null;
    }
    @Override
    public String write(ImageInstance inst) throws IOException {
        return null;
    }
}
