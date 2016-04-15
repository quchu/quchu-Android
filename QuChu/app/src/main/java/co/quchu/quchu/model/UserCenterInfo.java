package co.quchu.quchu.model;

/**
 * UserCenterInfo
 * User: Chenhs
 * Date: 2016-02-24
 * 用户中心model
 */
public class UserCenterInfo {


    /**
     * path : http://7xodsq.com1.z0.glb.clouddn.com/2@1?imageMogr2/thumbnail/800x/format/webp
     */

    private String backImgs = "";
    /**
     * backImg : {"path":"http://7xodsq.com1.z0.glb.clouddn.com/2@1?imageMogr2/thumbnail/800x/format/webp"}
     * cardNum : 0
     * followNum : 0
     * fovPlaceNum : 0
     * gender : 男
     * hostNum : 0
     * isFollow : false
     * location : null
     * name : 管理员
     * photo : http://7xodsq.com1.z0.glb.clouddn.com/1-app-default-avatar
     */
    public int userId = 0;
    private int cardNum = 0;
    private int followNum = 0;
    private int fovPlaceNum = 0;
    private String gender = "";
    private int hostNum = 0;
    private boolean isFollow = false;
    private Object location;
    private String name = "";
    private String photo = "";
    private int age;

    public String getBackImgs() {
        return backImgs;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public int getAge() {
        return age;
    }

    public void setBackImg(String backImg) {
        this.backImgs = backImg;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public void setFovPlaceNum(int fovPlaceNum) {
        this.fovPlaceNum = fovPlaceNum;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHostNum(int hostNum) {
        this.hostNum = hostNum;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBackImg() {
        return backImgs;
    }

    public int getCardNum() {
        return cardNum;
    }

    public int getFollowNum() {
        return followNum;
    }

    public int getFovPlaceNum() {
        return fovPlaceNum;
    }

    public String getGender() {
        return gender;
    }

    public int getHostNum() {
        return hostNum;
    }

    public boolean isIsFollow() {
        return isFollow;
    }

    public Object getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public static class BackImgEntity {
        private String path = "";

        public void setPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
