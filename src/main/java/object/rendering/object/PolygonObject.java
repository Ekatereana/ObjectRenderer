package object.rendering.object;

import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.scene.SceneObject;
import object.rendering.scene.Transform;

import java.util.List;

public class PolygonObject extends SceneObject {

    private List<Triangle> triangles;
    private Triangle intersection;

    public PolygonObject(Transform transform, List<Triangle> triangles) {
        super(transform);
        for (Triangle tria : triangles) {
            tria.getTransform().setParent(transform);
        }
        this.triangles = triangles;
    }

    @Override
    public boolean intersect(Ray ray) {
        intersection = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Triangle tria : triangles) {
            if (tria.intersect(ray)) {
                double distance = ray.getScale();
                if (distance < minDistance) {
                    minDistance = distance;
                    intersection = tria;
                }
            }
        }
        return intersection != null;
    }

    @Override
    public Vector3 getNormal(Vector3 hitPoint) {
        if (intersection == null) {
            return super.getNormal(hitPoint);
        }
        return intersection.getNormal(hitPoint);
    }
}
