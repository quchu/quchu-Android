package co.quchu.quchu.model;

/**
 * Created by Nico on 16/8/2.
 */
public class UpdateInfoModel {

    private int versionCode;
    private String versionName;
    private String downUrl;

    @Override
    public String toString() {
        return "UpdateInfoModel{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", downUrl='" + downUrl + '\'' +
                '}';
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }
}
