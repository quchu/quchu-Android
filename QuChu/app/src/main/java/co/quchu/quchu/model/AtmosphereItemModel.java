package co.quchu.quchu.model;

/**
 * AtmosphereItemModel
 * User: Chenhs
 * Date: 2015-10-30
 * 氛围列表 item model
 */
public class AtmosphereItemModel {


    /**
     * address : 思明区大学路211号13-14号店面(厦大医学院往沙坡尾方向100米)
     * cover : http://imgdn.paimeilv.com/18-default-place-cover
     * describe : 厦门,沙坡尾
     * height : 1024
     * latitude : 24.44319
     * longitude : 118.096081
     * name : 烧鸟居酒屋
     * pid : 18
     * rgb : 6a5458
     * takeIndex : 4
     * width : 1024
     */

    private String address;
    private String cover;
    private String describe;
    private int height;
    private String latitude;
    private String longitude;
    private String name;
    private int pid;
    private String rgb;
    private int takeIndex;
    private int width;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public void setTakeIndex(int takeIndex) {
        this.takeIndex = takeIndex;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getAddress() {
        return address;
    }

    public String getCover() {
        return cover;
    }

    public String getDescribe() {
        return describe;
    }

    public int getHeight() {
        return height;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public int getPid() {
        return pid;
    }

    public String getRgb() {
        return rgb;
    }

    public int getTakeIndex() {
        return takeIndex;
    }

    public int getWidth() {
        return width;
    }
}
