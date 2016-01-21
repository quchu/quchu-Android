package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * UserInfoModel
 * User: Chenhs
 * Date: 2015-11-30
 */
public class UserInfoModel implements Serializable {

    /**
     * fullname : 觉得解放军了一下下了了一个月后面
     * gender : 男
     * isweibo : false
     * isweixin : false
     * location : 厦门市
     * photo : http://7xo7et.com1.z0.glb.clouddn.com/88-app-default-avatar
     * token : 2175a92d7b88ac984e56d8dbf138a4a1dd0c4e49
     * userId : 88
     * username : 17750611071
     * type : login
     */

    private String fullname;
    private String gender;
    private boolean isweibo;
    private boolean isweixin;
    private String location;
    private String photo;
    private String token;
    private int userId;
    private String username;
    private String type;

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setIsweibo(boolean isweibo) {
        this.isweibo = isweibo;
    }

    public void setIsweixin(boolean isweixin) {
        this.isweixin = isweixin;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullname() {
        return fullname;
    }

    public String getGender() {
        return gender;
    }

    public boolean isIsweibo() {
        return isweibo;
    }

    public boolean isIsweixin() {
        return isweixin;
    }

    public String getLocation() {
        return location;
    }

    public String getPhoto() {
        return photo + "?" + System.currentTimeMillis();
    }

    public String getToken() {
        return token;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }
}
