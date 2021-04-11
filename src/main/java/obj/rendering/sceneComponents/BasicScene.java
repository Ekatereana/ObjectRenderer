package obj.rendering.sceneComponents;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BasicScene extends AbstractScene {
  private List<SceneComponent> entities;
  private Color background;

  public BasicScene(List<SceneComponent> entities, Color background) {
    this.entities = entities;
    this.background = background;
  }

  public BasicScene(List<SceneComponent> entities) {
    this(entities, new Color(0, 0, 0));
  }

  public BasicScene() {
    this(new ArrayList<>());
  }
  @Override
  public List<SceneComponent> getSceneObjects() {
    return entities;
  }

  @Override
  public List<LightSource> getLights() {
    return entities.stream()
            .filter(obj -> obj instanceof LightSource)
            .map(obj -> (LightSource) obj)
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
  public void addSceneObject(SceneComponent obj) {
    entities.add(obj);
  }

}