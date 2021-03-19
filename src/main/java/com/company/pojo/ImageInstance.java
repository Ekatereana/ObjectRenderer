package com.company.pojo;

import com.company.pojo.headers.Header;
import lombok.Data;

import com.company.enums.ImageType;

import java.io.InputStream;

@Data
public class ImageInstance {
    private ImageType sourceFormat;
    private String sourcePath;
    private String outputPath;
    private ImageType goalFormat;
    private InputStream is;
    private ColorSpace rgb;
    private Header header;
}
