package com.company.convectors;

import com.company.abstruct.ImageConvector;
import com.company.pojo.ImageInstance;
import com.company.pojo.ImageMappingException;

import java.io.IOException;
import java.io.InputStream;

public class BMPConvector extends ImageConvector {

    @Override
    public ImageInstance read(ImageInstance inst) {

        System.out.println("BMP read");
        return inst;
    }

    @Override
    public String readHeader(InputStream is, String fileName) throws ImageMappingException, IOException {
        return null;
    }

    @Override
    public String write(ImageInstance inst) throws IOException {
        super.write(inst);
        return null;
    }
}
