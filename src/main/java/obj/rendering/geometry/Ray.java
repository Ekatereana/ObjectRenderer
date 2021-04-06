package obj.rendering.geometry;

public class Ray {
    private Vector3 origin;
    private Vector3 direction;

    public Ray(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public void setOrigin() {
        this.origin = origin;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection() {
        this.direction = direction;
    }
}