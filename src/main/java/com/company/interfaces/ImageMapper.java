package com.company.interfaces;

import com.company.pojo.ImageInstance;

public interface ImageMapper {
    ImageInstance read(ImageInstance inst);

    ImageInstance write(ImageInstance inst);
}
