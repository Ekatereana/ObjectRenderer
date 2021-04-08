package obj.rendering;

import java.util.ArrayList;

import obj.rendering.geometry.MollerTrumbore;
import obj.rendering.geometry.Triangle;
import obj.rendering.geometry.Vector3;
import obj.rendering.parsers.ObjectLoader;
import obj.rendering.scenes.Camera;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectLoader loader = new ObjectLoader();
            //loader.load("C:\\Users\\ekate\\IdeaProjects\\RenderCow\\src\\main\\resources\\cow.obj");
            loader.load("C:\\ouroboroser\\programming\\kyb.obj");
            Vector3 pos = new Vector3(0,0,0);
            Camera camera = new Camera(pos);

            ArrayList<Vector3> pixels = new ArrayList<Vector3>();
            ArrayList<Vector3> ndsPixels = new ArrayList<Vector3>();
            ArrayList<Vector3> screenPixels = new ArrayList<Vector3>();
            ArrayList<Vector3> cameraPixels = new ArrayList<Vector3>();

            int xSize = 4;
            int ySize = 4;

            int[][] screen = new int[xSize][ySize];

            for (int y = 0; y < screen.length; y++) {
                for (int x = 0; x < screen.length; x++) {
                    screen[x][y] = 0;
                    Vector3 currentPixel = new Vector3(x, y, 0);
                    pixels.add(currentPixel);
                }
            }

            for(Vector3 p : pixels){
                Vector3 currentPixel = p.normalize(p, xSize, ySize);
                ndsPixels.add(currentPixel);
            }

            for(Vector3 p : ndsPixels){
                //System.out.println(p);
                Vector3 currentPixel = p.center(p);
                screenPixels.add(currentPixel);
            }

            double ratio = ySize/xSize;
            double fov = 60;

            double tg = Math.toRadians(fov/2);
            double result = Math.round(Math.tan(tg) * 100d) / 100d;

            for (Vector3 p : screenPixels) {
                double cameraPixelX = Math.round((2 * p.x - 1) * ratio * result * 100d) / 100d;
                double cameraPixelY = (1 - 2 * p.y) * ratio;

                Vector3 currentCameraPixel = new Vector3(cameraPixelX, cameraPixelY, 0);
                cameraPixels.add(currentCameraPixel);
            }

            Vector3 a = new Vector3(100, 0, 0);
            Vector3 b = new Vector3(0, 100, 0);
            Vector3 c = new Vector3(0, 0, 100);

            Triangle triangle = new Triangle(a, b, c);

            MollerTrumbore t = new MollerTrumbore();
            t.intersectsTriangle(triangle);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
