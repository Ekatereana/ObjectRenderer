package object.rendering.scene;

import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.object.Box;

public class SceneComponent {

  private static double DEFAULT_ALBEDO = 0.18;

  protected Transform transform;
  protected Mesh mesh;
  protected double albedo = DEFAULT_ALBEDO;

  public SceneComponent(Transform transform, Mesh mesh) {
    this.transform = transform;
    this.mesh = mesh;
  }

  public SceneComponent(Transform transform) {
    this(transform, new Mesh());
  }

  public SceneComponent() {
    this(new Transform(), new Mesh());
  }

  public Transform getTransform() {
    return transform;
  }

  public Mesh getMesh() {
    return mesh;
  }

  public boolean intersect(Ray ray) {
    return false;
  }

  public Vector3 getNormal(Vector3 hitPoint) { return hitPoint.subtract(transform.position()).normalize(); }

  public Box getBoundary() {
    return null;
  }

  public double albedo() {
    return albedo;
  }
}
