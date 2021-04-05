package obj.rendering.abstracts;

import java.util.List;

public class Polygon extends ObjectComponent {
    private List<Vertex> vertices;

    public Polygon(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}