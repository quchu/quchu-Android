package co.quchu.quchu.model;

/**
 * Created by Nico on 16/7/11.
 */
public class CommentModel {

    private String createDate;
    private String userName;
    private String userPhoneUrl;
    private String content;
    private String sourceContent;
    private String sourceUrl;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneUrl() {
        return userPhoneUrl;
    }

    public void setUserPhoneUrl(String userPhoneUrl) {
        this.userPhoneUrl = userPhoneUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceContent() {
        return sourceContent;
    }

    public void setSourceContent(String sourceContent) {
        this.sourceContent = sourceContent;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
