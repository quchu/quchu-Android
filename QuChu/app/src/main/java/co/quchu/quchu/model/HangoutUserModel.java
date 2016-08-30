package co.quchu.quchu.model;

/**
 * Created by Nico on 16/8/22.
 */
public class HangoutUserModel {

  private int gender; //0 male ,1 female
  private float similarity;
  private int userId;
  private String name;
  private String photo;
  private String mark;

  @Override public String toString() {
    return "HangoutUserModel{" +
        "gender=" + gender +
        ", similarity='" + similarity + '\'' +
        ", userId=" + userId +
        ", name='" + name + '\'' +
        ", photo='" + photo + '\'' +
        ", mark='" + mark + '\'' +
        '}';
  }

  public int getGender() {
    return gender;
  }

  public void setGender(int gender) {
    this.gender = gender;
  }

  public float getSimilarity() {
    return similarity;
  }

  public void setSimilarity(float similarity) {
    this.similarity = similarity;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }
}
