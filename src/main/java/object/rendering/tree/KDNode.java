package object.rendering.tree;

import object.rendering.geometry.Ray;
import object.rendering.object.Box;
import object.rendering.scene.SceneObject;

import java.util.List;

public class KDNode {
  Box boundaryBox;
  private KDNode[] children;

  private List<SceneObject> objects;

  public KDNode(List<SceneObject> objects, Box boundaryBox, KDNode[] children) {
    this.objects = objects;
    this.boundaryBox = boundaryBox;
    this.children = children;
  }


  public SceneObject intersect(Ray ray) {
    if (boundaryBox.intersect(ray)) {
      double minDist = Double.MAX_VALUE;
      SceneObject res = checkChildren(ray);
      if (res != null) {
        minDist = ray.getScale();
      }
      for (SceneObject obj : objects) {
        if (obj.intersect(ray)) {
          double dist = ray.getScale();
          if (dist < minDist) {
            minDist = dist;
            res = obj;
          }
        }
      }
      ray.setScale(minDist == Double.MAX_VALUE ? 1 : minDist);
      return res;
    }
    return null;
  }

  private SceneObject checkChildren(Ray ray) {
    SceneObject res = null;
    double minDist = Double.MAX_VALUE;
    for (KDNode node : children) {
      if (node == null) {
        continue;
      }
      SceneObject obj = node.intersect(ray);
      if (obj != null) {
        double dist = ray.getScale();
        if (dist < minDist) {
          res = obj;
          minDist = dist;
        }
      }
    }
    ray.setScale(minDist == Double.MAX_VALUE ? 1 : minDist);
    return res;
  }
}
