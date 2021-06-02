package object.rendering.scenes;

import lombok.SneakyThrows;
import object.rendering.RenderConfig;
import object.rendering.geometry.Vector3;
import object.rendering.io.obj.ParseObjFile;
import object.rendering.object.Box;
import object.rendering.object.OptimizedObject;
import object.rendering.object.Sphere;
import object.rendering.renders.render.material.MaterialType;
import object.rendering.scene.BasicScene;
import object.rendering.scene.SceneComponent;
import object.rendering.scene.Transform;

import java.awt.*;
import java.io.FileInputStream;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class DemoScene extends BasicScene implements RenderConfig {
    private static final String path = "C:\\Users\\ekate\\Desktop\\IdeaProjects\\ImageConvecter\\cow.obj";

    @SneakyThrows
    public DemoScene() {
        super();
        Sphere sphere = new Sphere(new Transform(new Vector3(6, 0, 0)), 3);
        sphere.getMesh().setMaterialType(MaterialType.REFLECTED);

        Sphere sphere2 = new Sphere(new Transform(new Vector3(6, -6, -1)), 1);
        sphere2.getMesh().setColor(Color.YELLOW);
        sphere2.getMesh().setMaterialType(MaterialType.MATT);

        Sphere sphere3 = new Sphere(new Transform(new Vector3(3, -7, -1)), 2);
        sphere3.getMesh().setColor(Color.GREEN);
        sphere3.getMesh().setMaterialType(MaterialType.REFLECTED);

        Box box = new Box(new Transform(new Vector3(2, 3, -15)), 25);
        box.getMesh().setColor(Color.RED);
        box.getMesh().setMaterialType(MaterialType.MATT);
        super.addSceneObject(box);
        super.addSceneObject(sphere);
        super.addSceneObject(sphere2);
        super.addSceneObject(sphere3);


    }
}
