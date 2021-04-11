package obj.rendering.sceneComponents;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractScene {

    public abstract List<SceneComponent> getSceneObjects();

    public abstract List<LightSource> getLights();

    public abstract Optional<Camera> getCamera();

    abstract Color getBackgroundColor();

    public abstract void addSceneObject(SceneComponent obj);

    public void addSceneObjects(SceneComponent... obj) {
        Arrays.stream(obj).forEach(this::addSceneObject);
    }
}
