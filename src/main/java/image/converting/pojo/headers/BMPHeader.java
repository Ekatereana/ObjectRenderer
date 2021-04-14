package image.converting.pojo.headers;

import lombok.Getter;

@Getter
public class BMPHeader extends Header{
    private int offset;
    private int compression;
    private int imageSize;
    private int numColors;


    public BMPHeader(int width, int height, int offset, int compression, int imageSize, int numColors) {
        super(width, height);
        this.offset = offset;
        this.compression = compression;
        this.imageSize = imageSize;
        this.numColors = numColors;
    }
}
