package com.company.pojo.headers;

import lombok.Data;

@Data
public class Header {
    private int width;
    private int height;
    private int depth;

    public Header(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }
}
