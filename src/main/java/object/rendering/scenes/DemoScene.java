package object.rendering.scenes;

import object.rendering.geometry.Vector3;
import object.rendering.object.Box;
import object.rendering.scene.BasicScene;
import object.rendering.scene.Transform;

public class DemoScene extends BasicScene {

  public DemoScene() {
    Box other = new Box(new Transform(new Vector3(0, -6, 0)), 10);
  }
}
