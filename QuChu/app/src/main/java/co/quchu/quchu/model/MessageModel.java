package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * MessageModel
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageModel implements Serializable {

    /**
     * come : follow
     * content : 关注了你
     * form : hahahhahh
     * formId : 3
     * formPhoto : http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-30
     * time : 2016-01-11 11:52:22
     * type : follow
     */

    private String come;
    private String content;
    private String form;
    private String formId;
    private String formPhoto;
    private String time;
    private String type;

    public void setCome(String come) {
        this.come = come;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void setFormPhoto(String formPhoto) {
        this.formPhoto = formPhoto;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCome() {
        return come;
    }

    public String getContent() {
        return content;
    }

    public String getForm() {
        return form;
    }

    public String getFormId() {
        return formId;
    }

    public String getFormPhoto() {
        return formPhoto;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }
}
