package object.rendering.geometry;

public class Ray {
  private Vector3 origin;
  private Vector3 direction;
  private double scale;

  public Ray(Vector3 origin, Vector3 direction, double scale) {
    this.origin = origin;
    this.direction = direction;
    this.scale = scale;
  }

  public Ray(Vector3 origin, Vector3 direction) {
    this(origin, direction, 1);
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  public double getScale() {
    return scale;
  }

  public Vector3 getOrigin() {
    return origin;
  }

  public void setOrigin(Vector3 origin) {
    this.origin = origin;
  }

  public Vector3 getDirection() {
    return direction;
  }

  public void setDirection(Vector3 direction) {
    this.direction = direction;
  }

  public Vector3 getPoint() {
    return origin.add(direction.multiply(scale));
  }
}
