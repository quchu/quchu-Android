package co.quchu.quchu.model;

/**
 * UserInfoModel
 * User: Chenhs
 * Date: 2015-11-30
 */
public class UserInfoModel {

    /**
     * fullname : 阿磐aaa
     * gender : 男
     * isweibo : false
     * isweixin : true
     * photo : http://wx.qlogo.cn/mmopen/icia6NrmNxOsxPzMjNvk1eIHIz5RruwuYEKlJyZPzbzgvkOuC6nlvFCF0uklXbZMAwFcRIgcwK7GeSncPSPRqjoasPWRiaX3NkI/0
     * token : d7ad802f9df056fd2e03200659f15af88f133237
     * userId : 113
     * username : 阿磐aaa
     */

    private String fullname;
    private String gender;
    private boolean isweibo;
    private boolean isweixin;
    private String photo;
    private String token;
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

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
