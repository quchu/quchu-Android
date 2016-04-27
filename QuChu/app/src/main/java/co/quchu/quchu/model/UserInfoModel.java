package co.quchu.quchu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * UserInfoModel
 * User: Chenhs
 * Date: 2015-11-30
 */
public class UserInfoModel implements Parcelable {

    /**
     * fullname : 觉得解放军了一下下了了一个月后面
     * gender : 男
     * isweibo : false
     * isweixin : false
     * location : 厦门市
     * photo : http://7xo7et.com1.z0.glb.clouddn.com/88-app-default-avatar
     * token : 2175a92d7b88ac984e56d8dbf138a4a1dd0c4e49
     * userId : 88
     * username : 00000000-1e98-8990-03cd
     * type : login
     */

    private String fullname;
    private String gender;
    private boolean isweibo;
    private boolean isweixin;
    private boolean isVisitors;
    private String location = "";
    private String photo;
    private String token;
    private int userId;
    private String username;
    private String type;
    private int age;// 来趣处的总共多少年
    private int cardCount;// 脚印的个数
    private boolean isphone;//是否绑定手机号

    public void setIsphone(boolean isphone) {
        this.isphone = isphone;
    }

    public boolean isphone() {
        return isphone;
    }

    public int getAge() {
        return age;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setIsweibo(boolean isweibo) {
        this.isweibo = isweibo;
    }

    public void setIsVisitors(boolean isVisitors) {
        this.isVisitors = isVisitors;
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

    public boolean isIsVisitors() {
        return isVisitors;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullname);
        dest.writeString(this.gender);
        dest.writeByte(isweibo ? (byte) 1 : (byte) 0);
        dest.writeByte(isweixin ? (byte) 1 : (byte) 0);
        dest.writeByte(isVisitors ? (byte) 1 : (byte) 0);
        dest.writeString(this.location);
        dest.writeString(this.photo);
        dest.writeString(this.token);
        dest.writeInt(this.userId);
        dest.writeString(this.username);
        dest.writeString(this.type);
        dest.writeInt(this.age);
        dest.writeInt(this.cardCount);
    }

    public UserInfoModel() {
    }

    protected UserInfoModel(Parcel in) {
        this.fullname = in.readString();
        this.gender = in.readString();
        this.isweibo = in.readByte() != 0;
        this.isweixin = in.readByte() != 0;
        this.isVisitors = in.readByte() != 0;
        this.location = in.readString();
        this.photo = in.readString();
        this.token = in.readString();
        this.userId = in.readInt();
        this.username = in.readString();
        this.type = in.readString();
        this.age = in.readInt();
        this.cardCount = in.readInt();
    }

    public static final Creator<UserInfoModel> CREATOR = new Creator<UserInfoModel>() {
        @Override
        public UserInfoModel createFromParcel(Parcel source) {
            return new UserInfoModel(source);
        }

        @Override
        public UserInfoModel[] newArray(int size) {
            return new UserInfoModel[size];
        }
    };

    @Override
    public String toString() {
        return "UserInfoModel{" +
                "fullname='" + fullname + '\'' +
                ", gender='" + gender + '\'' +
                ", isweibo=" + isweibo +
                ", isweixin=" + isweixin +
                ", isVisitors=" + isVisitors +
                ", location='" + location + '\'' +
                ", photo='" + photo + '\'' +
                ", token='" + token + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", age=" + age +
                ", cardCount=" + cardCount +
                '}';
    }
}
