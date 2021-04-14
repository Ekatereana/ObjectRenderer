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
        new Main().run();
    }

    private void run() throws IOException {
        Scene demoScene = new BasicScene();
        SceneObject empty = new SceneObject();
        ParseObjFile parser = new ParseObjFile();
        parser.load(new FileInputStream(new File(
//                "C:\\Users\\ekate\\Desktop\\IdeaProjects\\ImageConvecter\\src\\main\\java\\resources\\cow.obj"
                "C:\\Users\\ekate\\Desktop\\IdeaProjects\\ImageConvecter\\src\\main\\java\\resources\\car4.obj"
        )
        ));
        OptimizedObject obj = new OptimizedObject(new Transform(Vector3.ZERO, new Vector3(-1, 0, 0), new Vector3(10, 10, 10)),
                parser.getPolygons().stream().map(el -> (SceneObject) el).collect(Collectors.toList()));
        obj.getMesh().setColor(new Color(0, 255, 0));
        Camera camera = createCamera();
        Light light = new DistantLight(new Transform(new Vector3(0, 0, 0)), 40000);
        camera.getTransform().setParent(empty.getTransform());
        camera.lookAt(new Vector3(10, -10, 0), obj.getTransform().position());
        demoScene.addSceneObjects(camera);
        demoScene.addSceneObjects(obj);
        demoScene.addSceneObjects(light);

        BasicRaytracingRender render = new BasicRaytracingRender(demoScene);
        render.render(demoScene);

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
