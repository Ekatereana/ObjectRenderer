package object.rendering.object;


import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.scene.Transform;

public class Disk extends Plane {
  private static final double DEFAULT_SIZE = 1;

  private double radius;

  public Disk(Transform transform, double radius) {
    super(transform);
    this.radius = radius;
  }

  public Disk(Transform transform) {
    this(transform, DEFAULT_SIZE);
  }

  @Override
  public boolean intersect(Ray ray) {
    if (super.intersect(ray)) {
      Vector3 intersection = ray.getPoint();
      Vector3 direction = intersection.subtract(transform.position());
      double distance = direction.magnitudeSquared();
      if (distance > radius * radius) {
        ray.setScale(1);
        return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public Vector3 getNormal(Vector3 hitPoint) {
    return super.getNormal(hitPoint);
  }
}
