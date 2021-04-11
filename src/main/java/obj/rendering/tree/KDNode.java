package obj.rendering.tree;

import obj.rendering.Box;
import obj.rendering.geometry.Ray;
import obj.rendering.sceneComponents.SceneComponent;

import java.util.List;

public class KDNode {
  Box boundaryBox;
  private KDNode[] children;

  private List<SceneComponent> objects;

  public KDNode(List<SceneComponent> objects, Box boundaryBox, KDNode[] children) {
    this.objects = objects;
    this.boundaryBox = boundaryBox;
    this.children = children;
  }

  public SceneComponent intersect(Ray ray) {
    if (boundaryBox.intersect(ray)) {
      double minDist = Double.MAX_VALUE;
      SceneComponent res = checkChildren(ray);
      if (res != null) {
        minDist = ray.getScale();
      }
      for (SceneComponent obj : objects) {
        if (obj.intersect(ray)) {
          double dist = ray.getScale();
          if (dist < minDist) {
            minDist = dist;
            res = obj;
          }
        }
      }
      return res;
    }
    return null;
  }

  private SceneComponent checkChildren(Ray ray) {
    SceneComponent res = null;
    double minDist = Double.MAX_VALUE;
    for (KDNode node : children) {
      if (node == null) {
        continue;
      }
      SceneComponent obj = node.intersect(ray);
      if (obj != null) {
        double dist = ray.getScale();
        if (dist < minDist) {
          res = obj;
          minDist = dist;
        }
      }
    }
    return res;
  }
}
