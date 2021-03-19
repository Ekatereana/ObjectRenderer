package com.company.interfaces;

import com.company.pojo.ImageInstance;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageMapper {
    ImageInstance read(ImageInstance inst);

    String write(ImageInstance inst) throws IOException;
}
