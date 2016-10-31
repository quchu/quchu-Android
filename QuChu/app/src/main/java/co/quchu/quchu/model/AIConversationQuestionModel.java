package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/10/31.
 */

public class AIConversationQuestionModel {

  /**
   * flash : null
   * answerPramms : ["init_001_02"]
   * answer : 首先，做个自我介绍，我是Patricia你的智能【二货】小助手。
   * type : 1
   */

  private String flash;
  private String answer;
  private String type;
  private List<String> answerPramms;

  public String getFlash() {
    return flash;
  }

  public void setFlash(String flash) {
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
    return "AIConversationQuestionModel{" +
        "flash='" + flash + '\'' +
        ", answer='" + answer + '\'' +
        ", type='" + type + '\'' +
        ", answerPramms=" + answerPramms +
        '}';
  }
}
