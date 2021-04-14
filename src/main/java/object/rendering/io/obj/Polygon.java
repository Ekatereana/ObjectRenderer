package object.rendering.io.obj;

import java.util.List;

public class Polygon {
    private List<Vertex> vertices;

    public Polygon(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
