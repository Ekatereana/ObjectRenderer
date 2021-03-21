package com.company.pojo.headers;

import lombok.Data;

@Data
public class Header {
    private int width;
    private int height;
    private final int depth = 255;

    public Header(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
