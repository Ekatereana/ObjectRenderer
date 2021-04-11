package obj.rendering.sceneComponents;

import obj.rendering.geometry.Ray;
import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Triangle;

import java.util.List;

public class PolygonObject extends SceneComponent {

    private List<Triangle> triangles;
    private Triangle intersection;

    public PolygonObject(Transformation transform, List<Triangle> triangles) {
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
}
