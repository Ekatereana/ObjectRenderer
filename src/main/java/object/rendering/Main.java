package object.rendering;

import object.rendering.geometry.Vector3;
import object.rendering.graphics.BufferedImageRaster;
import object.rendering.graphics.Raster;
import object.rendering.io.obj.ParseObjFile;
import object.rendering.object.OptimizedObject;
import object.rendering.scene.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

public class Main {
    private static final int SCREEN_WIDTH = 640;
    private static final int SCREEN_HEIGHT = 640;

    public static void main(String[] args) throws IOException {
        new Main().start();
    }

    private void start() throws IOException {
        Vector3 objTransform1 = new Vector3(-1, 0, 0 );
        Vector3 objTransform2 = new Vector3(10, 10, 10);

        Vector3 lightDir = new Vector3(0, 0, 0);
        Vector3 cameraDir = new Vector3(10, -10, 0);

        Scene scene = new BasicScene();
        SceneComponent screen = new SceneComponent();
        ParseObjFile parser = new ParseObjFile();

        parser.load(new FileInputStream(new File("C:\\ouroboroser\\cow.obj")));

        OptimizedObject obj = new OptimizedObject(new Transform(Vector3.ZERO, objTransform1, objTransform2),
                parser.getPolygons().stream().map(el -> (SceneComponent) el).collect(Collectors.toList()));
        obj.getMesh().setColor(new Color(0, 255, 0));

        Camera camera = createCamera();
        Light light = new DistantLight(new Transform(lightDir), 40000);
        camera.getTransform().setParent(screen.getTransform());
        camera.lookAt(cameraDir, obj.getTransform().position());
        scene.addSceneObjects(camera);
        scene.addSceneObjects(obj);
        scene.addSceneObjects(light);

        BasicRaytracingRender render = new BasicRaytracingRender(scene);
        render.render(scene);

        File outputfile = new File("result2.png");
        try {
            ImageIO.write((BufferedImage) render.getRaster().toImage(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Camera createCamera() {
        Raster raster = new BufferedImageRaster(SCREEN_WIDTH, SCREEN_HEIGHT);
        return new Camera(raster, new Transform(Vector3.ZERO));
    }
}
