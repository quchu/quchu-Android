package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * UserInfoModel
 * User: Chenhs
 * Date: 2015-11-30
 */
public class UserInfoModel implements Serializable {


    /**
     * fullname : chslalalala
     * gender : 男
     * isweibo : true
     * isweixin : false
     * photo : http://tp1.sinaimg.cn/5230113944/50/40061324886/1
     * token : f3579a9a3d31e0075c1eaf50269f536b6c1daa99
     * type : login
     * userId : 11
     * username : chslalalala
     */

    private String fullname;
    private String gender;
    private boolean isweibo;
    private boolean isweixin;
    private String photo;
    private String token;
    private String type;
    private int userId;
    private String username;

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

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhoto() {
        return photo;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
