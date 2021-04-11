package obj.rendering;

import obj.rendering.abstracts.Polygon;
import obj.rendering.geometry.MollerTrumbore;
import obj.rendering.geometry.Triangle;
import obj.rendering.geometry.Vector3;
import obj.rendering.parsers.ObjectLoader;
import obj.rendering.sceneComponents.Camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectLoader loader = new ObjectLoader();
//            loader.load("C:\\Users\\ekate\\IdeaProjects\\RenderCow\\src\\main\\resources\\cow.obj");
            loader.load("C:\\Users\\ekate\\Desktop\\IdeaProjects\\ImageConvecter\\src\\main\\java\\resources\\piramid.obj");
            List<Triangle> triangles =
                    loader.getPolygons().stream().map(Main::mapToTriangle).collect(Collectors.toList());

            Vector3 pos = new Vector3(0, 0, 0);
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

            for (Vector3 p : pixels) {
                Vector3 currentPixel = p.normalize(p, xSize, ySize);
                ndsPixels.add(currentPixel);
            }

            for (Vector3 p : ndsPixels) {
                Vector3 currentPixel = p.center(p);
                screenPixels.add(currentPixel);
            }

            double ratio = ySize / xSize;
            double fov = 60;

            double tg = Math.toRadians(fov / 2);
            double result = Math.round(Math.tan(tg) * 100d) / 100d;

            for (Vector3 p : screenPixels) {
                double cameraPixelX = Math.round((2 * p.x - 1) * ratio * result * 100d) / 100d;
                double cameraPixelY = (1 - 2 * p.y) * ratio;
                Vector3 currentCameraPixel = new Vector3(cameraPixelX, cameraPixelY, 0);
                cameraPixels.add(currentCameraPixel);
            }

//            Vector3 a = new Vector3(-1, -1, -5);
//            Vector3 b = new Vector3(1, -1, -5);
//            Vector3 c = new Vector3(0, 1, -5);

//            a = a.normalize(a, xSize, ySize);
//            b = b.normalize(b, xSize, ySize);
//            c = c.normalize(c, xSize, ySize);


//            Vector3 a = new Vector3(3.13, 1.71, 0.81);
//            Vector3 b = new Vector3(4.08, 0.59, 1.68);
//            Vector3 c = new Vector3(1.4, 1.4, 2.31);

//            Vector3 a = new Vector3(100, 0, 0);
//            Vector3 b = new Vector3(0, 100, 0);
//            Vector3 c = new Vector3(0, 0, 100);

            Triangle triangle = triangles.get(0);
//            Vector3 dir = new Vector3(0,-0.1,-1);
//
            MollerTrumbore t = new MollerTrumbore();
//
////            var finalResult = t.intersectsTriangle(dir, triangle);
////            System.out.println(finalResult);
            Double finalResult;
            for (Vector3 p : cameraPixels) {
                finalResult = t.intersectsTriangle(p, triangle);
                if (finalResult != null) {
                    System.out.println(finalResult);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Triangle mapToTriangle(Polygon polygon) {
        return new Triangle(polygon.getVertices().get(0).geometry,
                polygon.getVertices().get(1).geometry, polygon.getVertices().get(2).geometry);
    }
}
