package obj.rendering.geometry;

import obj.rendering.sceneComponents.SceneComponent;

public class Triangle extends SceneComponent {
    private Vector3 a;
    private Vector3 b;
    private Vector3 c;

    public Triangle(Vector3 a, Vector3 b, Vector3 c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Triangle(Transformation transform, Vector3 v0, Vector3 v1, Vector3 v2) {
        super(transform);
        this.a = v0;
        this.b = v1;
        this.c = v2;
    }


    public Vector3 getA(Triangle t) {
        return t.a;
    }

    public Vector3 getB(Triangle t) {
        return t.b;
    }

    public Vector3 getC(Triangle t) {
        return t.c;
    }
}
