package object.rendering.scenes;

import object.rendering.geometry.Vector3;
import object.rendering.object.Box;
import object.rendering.object.Sphere;
import object.rendering.scene.BasicScene;
import object.rendering.scene.DistantLight;
import object.rendering.scene.Transform;

import java.awt.*;

public class DemoScene extends BasicScene {

  public DemoScene() {
    Box other = new Box(new Transform(new Vector3(0, -6, 0)), 10);
    Sphere sphere = new Sphere(new Transform(Vector3.FORWARD.multiply(-3)), 1);
    sphere.getMesh().setColor(Color.RED);
    addSceneObjects(
            sphere,
            new Box(new Transform(new Vector3(2, 2, -3)), 1),
            other,
            new DistantLight(new Transform(Vector3.ZERO, new Vector3(0, -1, 0)), 10)
            //new PointLight(new Transform(new Vector3(0, 0, -1), new Vector3(0, -1, 0)), 1000)
    );
  }
}
