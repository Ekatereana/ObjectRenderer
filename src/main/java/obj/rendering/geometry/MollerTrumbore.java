package obj.rendering.geometry;

public class MollerTrumbore {
    public void intersectsTriangle(Triangle triangle) {
        Vector3 vertexA = triangle.getA(triangle);
        Vector3 vertexB = triangle.getB(triangle);
        Vector3 vertexC = triangle.getC(triangle);

        Vector3 edge1 = vertexA.edge(vertexA, vertexC);
        Vector3 edge2 = vertexA.edge(vertexB, vertexC);
        Vector3 N = edge1.crossProduct(edge2);
    }
}
