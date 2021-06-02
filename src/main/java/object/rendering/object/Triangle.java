package object.rendering.object;


import lombok.Data;
import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.scene.SceneComponent;
import object.rendering.scene.Transform;
@Data
public class Triangle extends SceneComponent {
  private static final double DELTA = 1e-6;

  private Vector3 v0; // Points which define triangle
  private Vector3 v1;
  private Vector3 v2;

//  normals for vertexes
  private Vector3 n0;
  private Vector3 n1;
  private Vector3 n2;


  public Triangle(Transform transform,
                  Vector3 v0,
                  Vector3 n0,
                  Vector3 v1,
                  Vector3 n1,
                  Vector3 v2,
                  Vector3 n2) {
    super(transform);
    this.v0 = v0;
    this.v1 = v1;
    this.v2 = v2;
    this.n0 = n0;
    this.n1 = n1;
    this.n2 = n2;
  }

  @Override
  public boolean intersect(Ray ray) {
    Vector3 v0 = transform.applyPoint(this.v0);
    Vector3 v1 = transform.applyPoint(this.v1);
    Vector3 v2 = transform.applyPoint(this.v2);
    Vector3 v0v1 = v1.subtract(v0);
    Vector3 v0v2 = v2.subtract(v0);
    Vector3 direction = ray.getDirection();

    Vector3 p = direction.crossProduct(v0v2);
    double det = v0v1.dotProduct(p);
    if (Math.abs(det) < DELTA) {
      return false;
    }
    double invDet = 1 / det;

    Vector3 t = ray.getOrigin().subtract(v0);
    double u = t.dotProduct(p) * invDet;
    if (u < 0 || u > 1) {
      return false;
    }

    Vector3 q = t.crossProduct(v0v1);
    double v = direction.dotProduct(q) * invDet;
    if (v < 0 || v + u > 1) {
      return false;
    }


    double scale = v0v2.dotProduct(q) * invDet;
    if (scale < 0) {
      return false;
    }
    ray.setScale(scale);
    return true;
  }

  @Override
  public Box getBoundary() {
    Vector3 v0 = transform.applyPoint(this.v0);
    Vector3 v1 = transform.applyPoint(this.v1);
    Vector3 v2 = transform.applyPoint(this.v2);
    Vector3 lower = new Vector3(
            Math.min(Math.min(v0.x, v1.x), v2.x),
            Math.min(Math.min(v0.y, v1.y), v2.y),
            Math.min(Math.min(v0.z, v1.z), v2.z)
    );
    Vector3 upper = new Vector3(
            Math.max(Math.max(v0.x, v1.x), v2.x),
            Math.max(Math.max(v0.y, v1.y), v2.y),
            Math.max(Math.max(v0.z, v1.z), v2.z)
    );
    return new Box(new Transform(), lower, upper);
  }

  @Override
  public Vector3 getNormal(Vector3 hitPoint) {
    Vector3 v1v0 = v1.subtract(v0);
    Vector3 v2v0 = v2.subtract(v0);
    Vector3 p = hitPoint.crossProduct(v2v0);
    double det = v1v0.dotProduct(p);
    double invDet = 1 / det;
    Vector3 t = hitPoint.subtract(v0);
    double u = t.dotProduct(p) * invDet;
    Vector3 q = t.crossProduct(v1v0);
    double v = hitPoint.dotProduct(q) * invDet;
    double w = 1 - u - v;
    return n0.multiply(w).add(n1.multiply(u)).add(n2.multiply(v));
  }
}
