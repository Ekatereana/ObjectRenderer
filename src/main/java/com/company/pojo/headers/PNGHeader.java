package com.company.pojo.headers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNGHeader extends Header{
    private  int colorType;
    public PNGHeader(int width, int height, int colorType) {
        super(width, height);
        this.colorType = colorType;
    }
}
