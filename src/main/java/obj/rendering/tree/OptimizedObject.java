package obj.rendering.tree;

import obj.rendering.geometry.Ray;
import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Vector3;
import obj.rendering.sceneComponents.SceneComponent;

import java.util.List;

public class OptimizedObject extends SceneComponent {
  private KDTree tree;

  public OptimizedObject(Transformation transform, List<SceneComponent> objects) {
    super(transform);
    for (SceneComponent obj : objects) {
      obj.getTransform().setParent(transform);
    }
    tree = new KDTree(objects);
  }

  @Override
  public boolean intersect(Ray ray) {
    return tree.intersect(ray);
  }

  @Override
  public Box getBoundary() {
    return tree.boundary();
  }

  @Override
  public Vector3 getNormal(Vector3 hitPoint) {
    SceneComponent obj = tree.getIntersection();
    if (obj == null) {
      return super.getNormal(hitPoint);
    }
    return obj.getNormal(hitPoint);
  }
}
