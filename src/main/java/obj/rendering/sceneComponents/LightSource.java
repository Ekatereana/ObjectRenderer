package obj.rendering.sceneComponents;

import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Vector3;

public abstract class LightSource extends SceneComponent{
    private double intensity;

    public LightSource(Transformation transformation, double intensity) {
        super(transformation);
        this.intensity = intensity;
    }

    public double intensity() {
        return intensity;
    }

    public abstract double illuminate(Vector3 normal, double distance);

    public abstract double getMaxDist(Vector3 point);
}
