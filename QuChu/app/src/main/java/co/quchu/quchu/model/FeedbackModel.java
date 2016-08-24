package co.quchu.quchu.model;

/**
 * 用户反馈模型
 * <p/>
 * Created by mwb on 16/8/23.
 */
public class FeedbackModel {

    /**
     * feedbackId : 46
     * state : 0
     * title : 123456
     * value : 的风风光光个
     * createDate : 2016-08-24
     */

    private int feedbackId;
    private String state;
    private String title;
    private String value;
    private String createDate;

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
