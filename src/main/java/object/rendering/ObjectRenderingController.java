package object.rendering;

import image.converting.pojo.ColorSpace;
import object.rendering.geometry.Vector3;
import object.rendering.graphics.ArrayRaster;
import object.rendering.graphics.BufferedImageRaster;
import object.rendering.graphics.Raster;
import object.rendering.io.obj.ParseObjFile;
import object.rendering.object.OptimizedObject;
import object.rendering.renders.WhittedRayTracingRender;
import object.rendering.renders.render.material.MaterialType;
import object.rendering.scene.*;
import object.rendering.scenes.DemoScene;
import org.di.framework.annotations.Component;
import services.RenderService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class ObjectRenderingController implements RenderConfig, RenderService {

    @Override
    public ColorSpace render(String path) throws IOException {
//        Scene scene = new BasicScene();
        Scene scene = new DemoScene();
        SceneComponent screen = new SceneComponent();

        ParseObjFile parser = new ParseObjFile();
        parser.load(new FileInputStream(path));

        OptimizedObject obj = new OptimizedObject(new Transform(Vector3.ZERO, objTransform11, objTransform2),
                parser.getPolygons().stream().map(el -> (SceneComponent) el).collect(Collectors.toList()));
        obj.getMesh().setColor(new Color(150, 155, 255));
        obj.getMesh().setMaterialType(MaterialType.REFLECTED);

        Camera camera = createArrayCamera();
        Light light = new DistantLight(new Transform(lightDir), 35, lightDir);
        Light subLight = new DistantLight(new Transform(secondLightDir), 5, secondLightDir);
        camera.getTransform().setParent(screen.getTransform());
        camera.lookAt(cameraDir, Vector3.ZERO);
        scene.addSceneObjects(camera);
        scene.addSceneObject(obj);
        scene.addSceneObjects(light);
        scene.addSceneObjects(subLight);

        WhittedRayTracingRender render = new WhittedRayTracingRender(scene);
        render.render(scene);

        File outputfile = new File("one_light.png");
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
        return new Camera(raster, new Transform(Vector3.FORWARD));
    }

    private Camera createArrayCamera() {
        Raster raster = new ArrayRaster(SCREEN_WIDTH, SCREEN_HEIGHT);
        return new Camera(raster, new Transform(Vector3.ZERO));
    }


}
