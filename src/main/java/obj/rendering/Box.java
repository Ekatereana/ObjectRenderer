package obj.rendering;


import obj.rendering.geometry.Ray;
import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Vector3;
import obj.rendering.sceneComponents.SceneComponent;

public class Box extends SceneComponent {
  private static final double DELTA = 1e-6;
  private static final double DEFAULT_SIZE = 1;

  public Vector3 lowerBounds;
  public Vector3 upperBounds;

  public Box(Transformation transform, double size) {
    super(transform);
    Vector3 pos = transform.position();
    Vector3 bound = Vector3.ONE.multiply(size * 0.5);
    upperBounds = pos.add(bound);
    lowerBounds = pos.add(bound.multiply(-1));
  }

  public Box(Transformation transform, Vector3 lowerBounds, Vector3 upperBounds) {
    super(transform);
    this.lowerBounds = lowerBounds;
    this.upperBounds = upperBounds;
  }

  public Box(Transformation transform) {
    this(transform, DEFAULT_SIZE);
  }

  @Override
  public boolean intersect(Ray ray) {
    Vector3 direction = ray.getDirection();
    Vector3 origin = ray.getOrigin();
    double tMin, tMax, tyMin, tyMax, tzMin, tzMax;
    double x = axisDirection(direction.x);
    double y = axisDirection(direction.y);
    double z = axisDirection(direction.z);

    if (direction.x >= 0) {
      tMin = (lowerBounds.x - origin.x) * x;
      tMax = (upperBounds.x - origin.x) * x;
    } else {
      tMin = (upperBounds.x - origin.x) * x;
      tMax = (lowerBounds.x - origin.x) * x;
    }

    if (direction.y >= 0) {
      tyMin = (lowerBounds.y - origin.y) * y;
      tyMax = (upperBounds.y - origin.y) * y;
    } else {
      tyMin = (upperBounds.y - origin.y) * y;
      tyMax = (lowerBounds.y - origin.y) * y;
    }

    if (tMin > tyMax || tyMin > tMax) {
      return  false;
    }
    tMin = Math.max(tyMin, tMin);
    tMax = Math.min(tyMax, tMax);

    if (direction.z >= 0) {
      tzMin = (lowerBounds.z - origin.z) * z;
      tzMax = (upperBounds.z - origin.z) * z;
    } else {
      tzMin = (upperBounds.z - origin.z) * z;
      tzMax = (lowerBounds.z - origin.z) * z;
    }

    if (tMin > tzMax || tzMin > tMax) {
      return  false;
    }

    tMin = Math.max(tzMin, tMin);
    tMax = Math.min(tzMax, tMax);

    double t;
    if (tMin < 0) {
      if (tMax < 0) {
        return false;
      }
      t = tMax;
    } else {
      if (tMin > tMax) {
        t = tMax;
      } else {
        t = tMin;
      }
    }

    ray.setScale(t);
    return true;
  }

  @Override
  public Box getBoundary() {
    return new Box(transform, lowerBounds, upperBounds);
  }

  private double axisDirection(double val) {
    return Math.abs(val) < DELTA ? val >= 0 ? 1 / DELTA : -1 / DELTA : 1 / val;
  }
}
