package obj.rendering.sceneComponents;

import lombok.Data;
import lombok.Getter;
import obj.rendering.geometry.Vector3;

import java.util.ArrayList;

@Data
public class Screen {
    private static final double FOV = 90;

    @Getter
    private final int xSize;
    private final int ySize;
//    private final int [][] screen;
//    private final int [][] pixels;

    private double ratio;

    public Screen(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.ratio = ySize/xSize;
//        screen = new int[xSize][ySize];
//        pixels = new int[xSize][ySize];
    }


    public void fillPixels(ArrayList<Vector3> pixels) {
        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                Vector3 currentPixel = new Vector3(x, y, 0);
                pixels.add(currentPixel);
            }
        }
    }

    public ArrayList<Vector3> screen(ArrayList<Vector3> pixels) {
        ArrayList<Vector3> ndsPixels = new ArrayList<Vector3>();
        pixels.forEach(p -> {
            Vector3 currentPixel = p.normalize(p, xSize, ySize);
            currentPixel = currentPixel.center(currentPixel);
            ndsPixels.add(currentPixel);
        });
        return ndsPixels;
    }

    public ArrayList<Vector3> camera(ArrayList<Vector3> pixels, double result) {
        ArrayList<Vector3> cameraPixels = new ArrayList<Vector3>();
        pixels.forEach(p -> {
            double cameraPixelX = Math.round((2 * p.x - 1) * ratio * result * 100d) / 100d;
            double cameraPixelY = (1 - 2 * p.y) * ratio;
            Vector3 currentCameraPixel = new Vector3(cameraPixelX, cameraPixelY, 0);
            cameraPixels.add(currentCameraPixel);
        });
        return cameraPixels;
    }

    public double calculateAngle() {
        double tg = Math.toRadians(FOV/ 2);
        double result = Math.round(Math.tan(tg) * 100d) / 100d;
        return result;
    }
}
