package object.rendering;

import command.line.parser.instances.ImageInstance;
import image.converting.pojo.ColorSpace;
import object.rendering.geometry.Vector3;
import object.rendering.graphics.ArrayRaster;
import object.rendering.graphics.BufferedImageRaster;
import object.rendering.graphics.Raster;
import object.rendering.io.obj.ParseObjFile;
import object.rendering.object.OptimizedObject;
import object.rendering.object.Sphere;
import object.rendering.scene.*;
import object.rendering.scenes.DemoScene;
import org.di.framework.annotations.Autowired;
import org.di.framework.annotations.Component;
import services.RenderService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Component
public class ObjectRenderingController implements RenderConfig, RenderService {

    @Override
    public ColorSpace render(String path) throws IOException {
        Scene scene = new BasicScene();
        SceneComponent screen = new SceneComponent();

        ParseObjFile parser = new ParseObjFile();
        parser.load(new FileInputStream(path));
        OptimizedObject obj = new OptimizedObject(new Transform(Vector3.ZERO, objTransform1, objTransform2),
                parser.getPolygons().stream().map(el -> (SceneComponent) el).collect(Collectors.toList()));
        obj.getMesh().setColor(new Color(0, 255, 0));

        Camera camera = createArrayCamera();
        Light light = new DistantLight(new Transform(lightDir), 40000);
        Light subLight = new DistantLight(new Transform(secondLightDir), 2000);
        camera.getTransform().setParent(screen.getTransform());
        camera.lookAt(cameraDir, obj.getTransform().position());
        scene.addSceneObjects(camera);
        scene.addSceneObjects(obj);
        scene.addSceneObjects(light);
        Sphere sphere = new Sphere(new Transform(Vector3.FORWARD.multiply(5)), 1);
        sphere.getMesh().setColor(Color.RED);
        scene.addSceneObject(sphere);
//        scene.addSceneObjects(subLight);


        BasicRaytracingRender render = new BasicRaytracingRender(scene);
        render.render(scene);

        File outputfile = new File("result2.png");
        try {
            ImageIO.write((BufferedImage) render.getRaster().toImage(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayRaster raster = (ArrayRaster) render.getRaster();
        ColorSpace space = new ColorSpace(raster.getRed(), raster.getGreen(), raster.getBlue());
        return space;
    }

    private Camera createBufferCamera() {
        Raster raster = new BufferedImageRaster(SCREEN_WIDTH, SCREEN_HEIGHT);
        return new Camera(raster, new Transform(Vector3.ZERO));
    }

    private Camera createArrayCamera() {
        Raster raster = new ArrayRaster(SCREEN_WIDTH, SCREEN_HEIGHT);
        return new Camera(raster, new Transform(Vector3.ZERO));
    }


}
