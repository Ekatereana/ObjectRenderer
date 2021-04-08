package obj.rendering.geometry;

public class MollerTrumbore {
    private static final double EPSILON = 0.00001;

    public Number intersectsTriangle(Vector3 dir, Triangle triangle) {
        Vector3 origin = new Vector3(0, 0, 0);

        Vector3 vertexA = triangle.getA(triangle);
        Vector3 vertexB = triangle.getB(triangle);
        Vector3 vertexC = triangle.getC(triangle);

        Vector3 AB = vertexB.subtract(vertexA);
        Vector3 AC = vertexC.subtract(vertexA);

        Vector3 normalVector1 = dir.crossProduct(AC);
        double intPoint = AB.dotProduct(normalVector1);

        if (intPoint < EPSILON) {
            return null;
        }

        double invIntPoint = 1/intPoint;

        Vector3 pVector = origin.subtract(vertexA);
        double intPoint2 = pVector.dotProduct(normalVector1) * invIntPoint;

        if (intPoint2 < 0 || intPoint2 > 1) {
            return null;
        };

        Vector3 normalVector2 = pVector.crossProduct(AB);
        double intPoint3 = normalVector2.dotProduct(dir) * invIntPoint;

        if (intPoint3 < 0 || intPoint3 + intPoint3 >1 ) {
            return null;
        }

       double t = AC.dotProduct(normalVector2) * invIntPoint;
        return t;
    }
}
