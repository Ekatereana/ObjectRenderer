package image.converting.pojo.headers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNGHeader extends Header{
    private  int colorType;
    private int bitDepth;
    private int filter;
    private boolean order;
    public PNGHeader(int width, int height, int colorType, int bitDepth, int filter, boolean order) {
        super(width, height);
        this.colorType = colorType;
        this.bitDepth = bitDepth;
        this.filter = filter;
        this.order = order;
    }
}
