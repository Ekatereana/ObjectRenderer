package obj.rendering.scenes;

import obj.rendering.geometry.Vector3;

public class Camera {
    private Vector3 location;

    public Camera(Vector3 location) {
        this.location = location;
    }

    public Vector3 getLocation() {
        return location;
    }

    public void setLocation() {
        this.location = location;
    }
}
