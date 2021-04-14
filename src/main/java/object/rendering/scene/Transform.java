package object.rendering.scene;

import object.rendering.geometry.Matrix44;
import object.rendering.geometry.Vector3;

public class Transform {
  private Vector3 position;
  private Vector3 rotation;
  private Vector3 scale;
  private Matrix44 matrix;
  private Transform parent;

  public Transform(Vector3 position, Vector3 rotation) {
    this.position = position;
    this.rotation = rotation;
    this.scale = Vector3.ONE;
    matrix = new Matrix44();
    matrix.generateTransformation(position, rotation, scale);
  }

  public Transform(Vector3 position) {
    this(position, Vector3.ZERO);
  }

  public Transform() {
    this(Vector3.ZERO, Vector3.ZERO);
  }

  public Transform(Vector3 position, Vector3 rotation, Vector3 scale) {
    this.position = position;
    this.rotation = rotation;
    this.scale = scale;
    matrix = new Matrix44();
    matrix.generateTransformation(position, rotation, scale);
  }

  public Vector3 position() {
    return new Vector3(position);
  }

  public Vector3 rotation() {
    return new Vector3(rotation);
  }

  public Vector3 scale() {
    return new Vector3(scale);
  }

  public void setPosition(Vector3 position) {
    matrix.generateTransformation(position, rotation, scale);
    this.position = position;
  }

  public void setRotation(Vector3 rotation) {
    matrix.generateTransformation(position, rotation, scale);
    this.rotation = rotation;
  }

  public void setScale(Vector3 scale) {
    matrix.generateTransformation(position, rotation, scale);
    this.scale = scale;
  }

  /**
   * Apply transformation matrix to given vector.
   *
   * @param v vector to transform
   * @return transformed vector to local space
   */
  public Vector3 applyVector(Vector3 v) {
    if (parent != null) {
      v = parent.applyVector(v);
    }
    return matrix.applyVector(v);
  }

  /**
   * Apply transformation matrix to given point.
   *
   * @param v point to transform
   * @return transformed point to local space
   */
  public Vector3 applyPoint(Vector3 v) {
    if (parent != null) {
      v = parent.applyPoint(v);
    }
    return matrix.applyPoint(v);
  }

  /**
   * Create new transform matrix from given right, up, forward axis and translation.
   *
   * @param right normalized right vector
   * @param up normalized up vector
   * @param forward normalized forward vector
   * @param translate translation relatively to word coordinates
   */
  public void setMatrix(Vector3 right, Vector3 up, Vector3 forward, Vector3 translate) {
    matrix.setMatrix(right, up, forward, translate);
  }

  public void setParent(Transform parent) {
    this.parent = parent;
  }
}
