package obj.rendering;

import obj.rendering.abstracts.Polygon;
import obj.rendering.abstracts.Vertex;
import obj.rendering.geometry.MollerTrumbore;
import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Triangle;
import obj.rendering.geometry.Vector3;
import obj.rendering.graphics.BufferedImageRaster;
import obj.rendering.parsers.ObjectLoader;
import obj.rendering.sceneComponents.Camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectLoader loader = new ObjectLoader();
            loader.load("C:\\Users\\ekate\\Desktop\\IdeaProjects\\ImageConvecter\\src\\main\\java\\resources\\cow.obj");
//            loader.load("C:\\Users\\ekate\\Desktop\\IdeaProjects\\ImageConvecter\\src\\main\\java\\resources\\piramid.obj");
            List<Triangle> triangles =
                    loader.getPolygons().stream().map(Main::mapToTriangle).flatMap(i -> i.stream()).collect(Collectors.toList());

            Vector3 pos = new Vector3(0, 0, 0);
            Camera camera = new Camera(pos);

            ArrayList<Vector3> pixels = new ArrayList<Vector3>();
            ArrayList<Vector3> ndsPixels = new ArrayList<Vector3>();
            ArrayList<Vector3> screenPixels = new ArrayList<Vector3>();
            ArrayList<Vector3> cameraPixels = new ArrayList<Vector3>();

            int xSize = 640;
            int ySize = 640;

            int[][] screen = new int[xSize][ySize];

            for (int y = 0; y < screen.length; y++) {
                for (int x = 0; x < screen.length; x++) {
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

            screenPixels.forEach(p -> {
                double cameraPixelX = Math.round((2 * p.x - 1) * ratio * result * 100d) / 100d;
                double cameraPixelY = (1 - 2 * p.y) * ratio;
                Vector3 currentCameraPixel = new Vector3(cameraPixelX, cameraPixelY, 0);
                cameraPixels.add(currentCameraPixel);

            });
            BufferedImageRaster raster = new BufferedImageRaster(xSize, ySize);
            MollerTrumbore t = new MollerTrumbore();
            triangles.forEach(triangle ->
                    {
                        int x = 0;
                        for (int i = 0; i < cameraPixels.size(); i++) {
                            x = (int) i / xSize;
                            var finalResult = t.intersectsTriangle(cameraPixels.get(i), triangle);
                            if (finalResult != null) {
                                System.out.println(finalResult);
                                raster.setPixel(x, i - x * ySize, (byte) 255, (byte) 0, (byte) 0);
                            } else {
//                                raster.setPixel(x, i - x*ySize, (byte)255, (byte)255, (byte)255);
                            }

                        }
                    }

            );

            File outputfile = new File("result.png");
            try {
                ImageIO.write((BufferedImage) raster.mapToImage(), "png", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            for (Vector3 p : screenPixels) {
//                double cameraPixelX = Math.round((2 * p.x - 1) * ratio * result * 100d) / 100d;
//                double cameraPixelY = (1 - 2 * p.y) * ratio;
//                Vector3 currentCameraPixel = new Vector3(cameraPixelX, cameraPixelY, 0);
//                cameraPixels.add(currentCameraPixel);
//            }

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

//
////            var finalResult = t.intersectsTriangle(dir, triangle);
////            System.out.println(finalResult);

//            for (Vector3 p : cameraPixels) {
//                var finalResult = t.intersectsTriangle(p, triangle);
//                if (finalResult != null) {
//                    System.out.println(finalResult);
//                }
//
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Triangle> mapToTriangle(Polygon polygon) {
//        return new Triangle(polygon.getVertices().get(0).geometry,
//                polygon.getVertices().get(1).geometry, polygon.getVertices().get(2).geometry);
        List<Triangle> triangles = new ArrayList<>();

        List<Vertex> ver = polygon.getVertices();
        Vector3 pillar = ver.get(0).geometry;
        for (int i = 1; i < ver.size() - 1; i++) {
            triangles.add(new Triangle(new Transformation(),
                    pillar, ver.get(i).geometry, ver.get(i + 1).geometry));
        }
        System.out.println(triangles.size());
        return triangles;
    }
}
