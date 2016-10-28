package co.quchu.quchu.model;

import java.io.Serializable;
import java.util.List;

/**
 * 用户反馈模型
 * <p/>
 * Created by mwb on 16/8/23.
 */
public class FeedbackModel implements Serializable{

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
    /**
     * yphone : http://7xodsq.com1.z0.glb.clouddn.com/69-app-default-avatar
     * iphone : http://7xo7f0.com1.z0.glb.clouddn.com/app-default-avatar-46
     * msgList : [{"content":"好的","type":"0","createDate":"2016-08-18"},{"content":"没错","type":"1","createDate":"2016-08-18"},{"content":"无语了","type":"0","createDate":"2016-08-18"}]
     */

    private String yphone;
    private String iphone;
    /**
     * content : 好的
     * type : 0
     * createDate : 2016-08-18
     */

    private List<MsgListBean> msgList;

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

    public String getYphone() {
        return yphone;
    }

    public void setYphone(String yphone) {
        this.yphone = yphone;
    }

    public String getIphone() {
        return iphone;
    }

    public void setIphone(String iphone) {
        this.iphone = iphone;
    }

    public List<MsgListBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MsgListBean> msgList) {
        this.msgList = msgList;
    }

    public static class MsgListBean {
        private String content;
        private String type;
        private String createDate;

        private String userName;
        private boolean isHideUserInfo;

        public boolean isHideUserInfo() {
            return isHideUserInfo;
        }

        public void setHideUserInfo(boolean hideUserInfo) {
            isHideUserInfo = hideUserInfo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
