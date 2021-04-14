package object.rendering.scene;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicScene extends Scene {
  private List<SceneObject> entities;
  private Color background;

  public BasicScene(List<SceneObject> entities, Color background) {
    this.entities = entities;
    this.background = background;
  }

  public BasicScene(List<SceneObject> entities) {
    this(entities, new Color(0, 0, 0));
  }

  public BasicScene() {
    this(new ArrayList<>());
  }

  @Override
  public List<SceneObject> getSceneObjects() {
    return entities;
  }

  @Override
  public List<Light> getLights() {
    return entities.stream()
            .filter(obj -> obj instanceof Light)
            .map(obj -> (Light) obj)
            .collect(Collectors.toList());
  }

  @Override
  public Optional<Camera> getCamera() {
    return entities.stream()
            .filter(obj -> obj instanceof Camera)
            .map(obj -> (Camera) obj)
            .findFirst();
  }

  @Override
  public Color getBackgroundColor() {
    return background;
  }

  @Override
  public void addSceneObject(SceneObject obj) {
    entities.add(obj);
  }
}
