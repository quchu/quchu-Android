package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * Created by Nico on 16/8/30.
 */
public class SimpleCircleModel implements Serializable {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
