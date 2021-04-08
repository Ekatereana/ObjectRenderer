package obj.rendering.geometry;

public class Triangle {
    private Vector3 a;
    private Vector3 b;
    private Vector3 c;

    public Triangle(Vector3 a, Vector3 b, Vector3 c) {
        this.a = a;
        this.b = b;
        this.c = c;
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
