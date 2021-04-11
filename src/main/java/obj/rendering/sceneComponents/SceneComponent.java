package obj.rendering.sceneComponents;

import lombok.Getter;
import obj.rendering.tree.Box;
import obj.rendering.geometry.Ray;
import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Vector3;

public class SceneComponent {
    private static double DEFAULT_ALBEDO = 0.18;

    @Getter
    protected Transformation transform;
    @Getter
    protected Mesh mesh;
    protected double albedo = DEFAULT_ALBEDO;

    public SceneComponent(Transformation transform, Mesh mesh) {
        this.transform = transform;
        this.mesh = mesh;
    }

    public SceneComponent(Transformation transform) {
        this(transform, new Mesh());
    }

    public SceneComponent() {
        this(new Transformation(), new Mesh());
    }

    public boolean intersect(Ray ray) {
        return false;
    }

    public Vector3 getNormal(Vector3 hitPoint) { return Vector3.ZERO; }

    public Box getBoundary() {
        return null;
    }

    public double albedo() {
        return albedo;
    }
}
