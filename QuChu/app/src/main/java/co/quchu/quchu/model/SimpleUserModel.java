package co.quchu.quchu.model;

/**
 * Created by Nikolai on 2016/4/17.
 */
public class SimpleUserModel {

    private String userId;
    private String userName;
    private String userPhoneUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
