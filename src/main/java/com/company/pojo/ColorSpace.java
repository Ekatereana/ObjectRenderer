package com.company.pojo;

import lombok.Data;

@Data
public class ColorSpace {
    private int[][] r;
    private int[][] b;
    private int[][] g;

    public ColorSpace(int[][] r, int[][] b, int[][] g) {
        this.r = r;
        this.b = b;
        this.g = g;
    }
}
