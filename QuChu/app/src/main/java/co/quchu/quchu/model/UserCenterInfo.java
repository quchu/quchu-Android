package co.quchu.quchu.model;

import java.io.Serializable;

/**
 * UserCenterInfo
 * User: Chenhs
 * Date: 2016-02-24
 * 用户中心model
 */
public class UserCenterInfo implements Serializable{

    /**
     * age : 1
     * backImg : {"path":"http://7xodsq.com1.z0.glb.clouddn.com/3EB5CDA9-F8F5-45ED-A5BC-CFC75AA381B8?imageMogr2/thumbnail/800x/format/webp","width":293,"rgb":"909090","height":220}
     * cardNum : 38
     * followNum : 1
     * fovPlaceNum : 2
     * gender : 男
     * hostNum : 0
     * isFollow : true
     * location : 北京
     * mark : 艺术家
     * name : 003
     * photo : http://7xodsq.com1.z0.glb.clouddn.com/83-app-default-avatar
     */

    private int age;
    /**
     * path : http://7xodsq.com1.z0.glb.clouddn.com/3EB5CDA9-F8F5-45ED-A5BC-CFC75AA381B8?imageMogr2/thumbnail/800x/format/webp
     * width : 293
     * rgb : 909090
     * height : 220
     */

    private BackImgBean backImg;
    private int cardNum;
    private int followNum;
    private int fovPlaceNum;
    private String gender;
    private int hostNum;
    private boolean isFollow;
    private String location;
    private String mark;
    private String name;
    private String photo;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BackImgBean getBackImg() {
        return backImg;
    }

    public void setBackImg(BackImgBean backImg) {
        this.backImg = backImg;
    }

    public int getCardNum() {
        return cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getFovPlaceNum() {
        return fovPlaceNum;
    }

    public void setFovPlaceNum(int fovPlaceNum) {
        this.fovPlaceNum = fovPlaceNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHostNum() {
        return hostNum;
    }

    public void setHostNum(int hostNum) {
        this.hostNum = hostNum;
    }

    public boolean isIsFollow() {
        return isFollow;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static class BackImgBean  implements Serializable{
        private String path;
        private int width;
        private String rgb;
        private int height;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getRgb() {
            return rgb;
        }

        public void setRgb(String rgb) {
            this.rgb = rgb;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
