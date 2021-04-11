package obj.rendering.geometry;

import lombok.Data;

@Data
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
        this.origin = origin;
        this.direction = direction;
    }
}
