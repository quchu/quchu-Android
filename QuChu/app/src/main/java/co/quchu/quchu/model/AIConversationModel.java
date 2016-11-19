package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/11/2.
 */

public class AIConversationModel {
  private String answer;
  private String flash;
  private String type;

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }

  private long timeStamp;
  private List<String> answerPramms;
  private List<DetailModel> placeList;

  public List<DetailModel> getPlaceList() {
    return placeList;
  }

  public void setPlaceList(List<DetailModel> placeList) {
    this.placeList = placeList;
  }

  private EnumDataType dataType = EnumDataType.QUESTION;

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getFlash() {
    return flash;
  }

  public void setFlash(String flash) {
    this.flash = flash;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<String> getAnswerPramms() {
    return answerPramms;
  }

  public void setAnswerPramms(List<String> answerPramms) {
    this.answerPramms = answerPramms;
  }

  public EnumDataType getDataType() {
    return dataType;
  }

  public void setDataType(EnumDataType dataType) {
    this.dataType = dataType;
  }

  public enum EnumDataType {
    QUESTION, ANSWER, OPTION,GALLERY, NO_NETWORK
  }

  @Override public String toString() {
    return "AIConversationModel{" +
        "answer='" + answer + '\'' +
        ", flash='" + flash + '\'' +
        ", type='" + type + '\'' +
        ", timeStamp=" + timeStamp +
        ", answerPramms=" + answerPramms +
        ", placeList=" + placeList +
        ", dataType=" + dataType +
        '}';
  }
}
