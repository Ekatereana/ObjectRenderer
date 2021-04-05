package image.converting.pojo;

import lombok.Data;

@Data
public class ColorSpace {
    private int[][] r;
    private int[][] g;
    private int[][] b;

    public ColorSpace(int[][] r, int[][] g, int[][] b) {
        this.r = r;
        this.b = b;
        this.g = g;
    }
}
