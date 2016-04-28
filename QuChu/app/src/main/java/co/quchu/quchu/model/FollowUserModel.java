package co.quchu.quchu.model;

/**
 * FollowUserModel
 * User: Chenhs
 * Date: 2016-03-01
 */
public class FollowUserModel {

    /**
     * name : 白
     * photo : http://wx.qlogo.G5RC/0
     * userId : 94
     */

    private String name;
    private String photo;
    private int userId;
    private String location;
    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public int getUserId() {
        return userId;
    }
}
