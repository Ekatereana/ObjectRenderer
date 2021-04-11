package obj.rendering.sceneComponents;

import java.awt.*;

public class Mesh {
    private Color color;

    public Mesh(Color color) {
        this.color = color;
    }

    public Mesh() {
        this(new Color(255, 255, 255));
    }

    public Color color() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
