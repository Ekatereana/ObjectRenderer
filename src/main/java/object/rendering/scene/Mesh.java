package object.rendering.scene;

import lombok.Getter;
import lombok.Setter;
import object.rendering.renders.render.material.MaterialType;

import java.awt.*;

public class Mesh {

  private Color color;

  @Getter
  @Setter
  private MaterialType materialType;

  public Mesh(Color color) {
    this.color = color;
  }


  public Mesh() {
    this(null);
  }

  public Color color() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
