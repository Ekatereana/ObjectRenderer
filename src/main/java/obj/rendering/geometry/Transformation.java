package obj.rendering.geometry;

public class Transformation {
    private Vector3 position;
    private Vector3 rotation;
    private Vector3 scale;
    private Matrix4X4 matrix;
    private Transformation parent;

    public Transformation(Vector3 position, Vector3 rotation) {
        this.position = position;
        this.rotation = rotation;
        this.scale = Vector3.ONE;
        matrix = new Matrix4X4();
        matrix.generateTransformation(position, rotation, scale);
    }

    public Transformation(Vector3 position) {
        this(position, Vector3.ZERO);
    }

    public Transformation() {
        this(Vector3.ZERO, Vector3.ZERO);
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

    public Vector3 applyVector(Vector3 v) {
        if (parent != null) {
            v = parent.applyVector(v);
        }
        return matrix.applyVector(v);
    }

    public Vector3 applyPoint(Vector3 v) {
        if (parent != null) {
            v = parent.applyPoint(v);
        }
        return matrix.applyPoint(v);
    }

    public void setMatrix(Vector3 right, Vector3 up, Vector3 forward, Vector3 translate) {
        matrix.setMatrix(right, up, forward, translate);
    }

    public void setParent(Transformation parent) {
        this.parent = parent;
    }
}
