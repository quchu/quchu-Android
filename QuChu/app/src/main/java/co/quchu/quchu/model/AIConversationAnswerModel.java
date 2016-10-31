package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/10/31.
 */

public class AIConversationAnswerModel extends QAModel{

  /**
   * flash : null
   * answerPramms : ["init_001_03"]
   * answer : 别偷笑了，当我看不见是不是？
   * type : 1
   */

  private Object flash;
  private String answer;
  private String type;
  private List<String> answerPramms;

  public Object getFlash() {
    return flash;
  }

  public void setFlash(Object flash) {
    this.flash = flash;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
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

  @Override public String toString() {
    return "AIConversationAnswerModel{" +
        "flash=" + flash +
        ", answer='" + answer + '\'' +
        ", type='" + type + '\'' +
        ", answerPramms=" + answerPramms +
        '}';
  }
}
