package obj.rendering.geometry;

public class Vector3 {
    // Accuracy of comparing two double values
    private static final double COORDINATES_PRECISION = 1e-10;

    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 RIGHT = new Vector3(1, 0, 0);
    public static final Vector3 UP = new Vector3(0, 1, 0);
    public static final Vector3 FORWARD = new Vector3(0, 0, 1);
    public static final Vector3 ONE = new Vector3(1, 1, 1);

    public final double x;
    public final double y;
    public final double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public double magnitudeSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3 normalize() {
        double magnitude = magnitude();
        if (magnitude > 0) {
            double inverseMagnitude = 1 / magnitude;
            return multiply(inverseMagnitude);
        }
        return multiply(1);
    }

    public double distance(Vector3 v) {
        return subtract(v).magnitude();
    }

    public double dotProduct(Vector3 v) {
        return v.x * x + v.y * y + v.z * z;
    }

    public Vector3 crossProduct(Vector3 v) {
        return new Vector3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
        );
    }

    public Vector3 multiply(double t) {
        return new Vector3(x * t, y * t, z * t);
    }

    public Vector3 subtract(Vector3 v) {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Vector3 v = (Vector3) obj;
        return Math.abs(v.x - x) < COORDINATES_PRECISION
                && Math.abs(v.y - y) < COORDINATES_PRECISION
                && Math.abs(v.z - z) < COORDINATES_PRECISION;
    }

    @Override
    public int hashCode() {
        return (int) (x + y + z);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return x + " | " + y + " | " + z;
    }
}
