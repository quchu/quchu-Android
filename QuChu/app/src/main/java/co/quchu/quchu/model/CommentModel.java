package co.quchu.quchu.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nico on 16/7/11.
 */
public class CommentModel implements Serializable{

    private String createDate;
    private String userName;
    private String userPhoneUrl;
    private String content;
    private String sourceContent;
    private String sourceUrl;
    private String pqUrl;
    private int score;
    private int prId;
    private List<CommentImageModel> imageList;

    public List<CommentImageModel> getImageList() {
        return imageList;
    }

    public void setImageList(List<CommentImageModel> imageList) {
        this.imageList = imageList;
    }

    public String getPqUrl() {
        return pqUrl;
    }

    public void setPqUrl(String pqUrl) {
        this.pqUrl = pqUrl;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPrId() {
        return prId;
    }

    public void setPrId(int prId) {
        this.prId = prId;
    }

    private boolean collapsed = true;

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

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
