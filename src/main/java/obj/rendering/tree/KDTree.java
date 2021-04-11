package obj.rendering.tree;

import lombok.Getter;
import obj.rendering.geometry.Ray;
import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Vector3;
import obj.rendering.sceneComponents.SceneComponent;

import java.util.ArrayList;
import java.util.List;

public class KDTree {
  private static final double MIN_NUMBER_OF_OBJECTS = 3;
  private KDNode root;
  @Getter
  private SceneComponent intersection;

  public KDTree(List<SceneComponent> objects) {
    Box box = getBoundary(objects);
    root = buildNode(objects, box);
  }

  private Box getBoundary(List<SceneComponent> objects) {
    if (objects.size() == 0) {
      return new Box(new Transformation(), Vector3.ZERO, Vector3.ZERO);
    }
    Vector3 lowerBound = new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
    Vector3 upperBound = new Vector3(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    for (SceneComponent obj : objects) {
      Box boundary = obj.getBoundary();
      if (boundary == null) {
        continue;
      }
      lowerBound = new Vector3(
              Math.min(lowerBound.x, boundary.lowerBounds.x),
              Math.min(lowerBound.y, boundary.lowerBounds.y),
              Math.min(lowerBound.z, boundary.lowerBounds.z)
      );
      upperBound = new Vector3(
              Math.max(upperBound.x, boundary.upperBounds.x),
              Math.max(upperBound.y, boundary.upperBounds.y),
              Math.max(upperBound.z, boundary.upperBounds.z)
      );
    }
    if (lowerBound.equals(Vector3.ONE.multiply(Double.MAX_VALUE))) {
      lowerBound = Vector3.ONE.multiply(Double.MIN_VALUE);
    }
    if (upperBound.equals(Vector3.ONE.multiply(Double.MIN_VALUE))) {
      upperBound = Vector3.ONE.multiply(Double.MAX_VALUE);
    }
    return new Box(new Transformation(), lowerBound, upperBound);
  }

  private KDNode buildNode(List<SceneComponent> objects, Box box) {
    if (objects.size() <= MIN_NUMBER_OF_OBJECTS) {
      return new KDNode(objects, box, new KDNode[0]);
    }
    KDNode[] children = getChildren(objects, box);
    return new KDNode(objects, box, children);
  }

  private KDNode[] getChildren(List<SceneComponent> objects, Box box) {
    Vector3 mid  = new Vector3(
            (box.upperBounds.x + box.lowerBounds.x) / 2,
            (box.upperBounds.y + box.lowerBounds.y) / 2,
            (box.upperBounds.z + box.lowerBounds.z) / 2
    );
    Vector3 u = box.upperBounds;
    Vector3 l = box.lowerBounds;
    KDNode[] children = new KDNode[8];
    Box b = new Box(new Transformation(), mid, u);
    children[0] = buildNode(getObjectInBox(objects, b), b);
    b = new Box(new Transformation(), new Vector3(l.x, mid.y, mid.z), new Vector3(mid.x, u.y, u.z));
    children[1] = buildNode(getObjectInBox(objects, b), b);
    b = new Box(new Transformation(), new Vector3(mid.x, l.y, mid.z), new Vector3(u.x, mid.y, u.z));
    children[2] = buildNode(getObjectInBox(objects, b), b);
    b = new Box(new Transformation(), new Vector3(l.x, l.y, mid.z), new Vector3(mid.x, mid.y, u.z));
    children[3] = buildNode(getObjectInBox(objects, b), b);
    b = new Box(new Transformation(), new Vector3(mid.x, mid.y, l.z), new Vector3(u.x, u.y, mid.z));
    children[4] = buildNode(getObjectInBox(objects, b), b);
    b = new Box(new Transformation(), new Vector3(l.x, mid.y, l.z), new Vector3(mid.x, u.y, mid.z));
    children[5] = buildNode(getObjectInBox(objects, b), b);
    b = new Box(new Transformation(), new Vector3(mid.x, l.y, l.z), new Vector3(u.x, mid.y, mid.z));
    children[6] = buildNode(getObjectInBox(objects, b), b);
    b = new Box(new Transformation(), l, mid);
    children[7] = buildNode(getObjectInBox(objects, b), b);
    return children;
  }

  private List<SceneComponent> getObjectInBox(List<SceneComponent> objects, Box box) {
    ArrayList<SceneComponent> res = new ArrayList<>();
    for (SceneComponent obj : objects) {
      Box boundary = obj.getBoundary();
      if (boundary == null) {
        continue;
      }
      if (boxInBox(boundary, box)) {
        res.add(obj);
      }
    }
    for (SceneComponent obj : res) {
      objects.remove(obj);
    }
    return res;
  }

  private boolean boxInBox(Box in, Box out) {
    return in.upperBounds.x <= out.upperBounds.x
            && in.upperBounds.y <= out.upperBounds.y
            && in.upperBounds.z <= out.upperBounds.z
            && in.lowerBounds.x >= out.lowerBounds.x
            && in.lowerBounds.y >= out.lowerBounds.y
            && in.lowerBounds.z >= out.lowerBounds.z;
   }

  public boolean intersect(Ray ray) {
    intersection = root.intersect(ray);
    return intersection != null;
  }
  public Box boundary() {
    return root.boundaryBox;
  }
}
