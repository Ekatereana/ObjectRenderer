package com.company.interfaces;

import com.company.pojo.ImageInstance;
import com.company.pojo.ImageMappingException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface ImageMapper {
    ImageInstance read(ImageInstance inst);
//    reader helpers
    Object readHeader(InputStream is, String fileName) throws ImageMappingException, IOException;

    String write(ImageInstance inst) throws IOException, ImageMappingException;
}
