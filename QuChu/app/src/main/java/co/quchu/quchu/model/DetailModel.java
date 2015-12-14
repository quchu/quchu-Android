package co.quchu.quchu.model;

import java.util.List;

/**
 * DetailModel
 * User: Chenhs
 * Date: 2015-12-13
 */
public class DetailModel {

    /**
     * address : 鹭江道3号和平码头当丰新天地3楼302室
     * businessHours : 10:00-00:00
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/4-default-app-place-cover?imageMogr2/format/webp
     * genes : [{"key":"美食","value":"89"}]
     * height : 605
     * icons : [{"zh":"现金"}]
     * imglist : [{"cid":86,"cindex":0,"height":605,"imgpath":"http://7xo7et.com1.z0.glb.clouddn.com/4@1?imageMogr2/format/webp","width":909}]
     * isf : false
     * isout : false
     * latitude : 24.45613747484
     * longitude : 118.08450240938
     * name : ReNext里遇
     * nearbySpot :
     * net :
     * pid : 4
     * price : 128
     * restDay :
     * rgb : 6b4947
     * suggest : 4
     * tags : [{"zh":"下午茶"}]
     * tel : 0592-2233995
     * traffic :
     * width : 909
     */

    private String address;
    private String businessHours;
    private String cover;
    private int height;
    private boolean isf;
    private boolean isout;
    private String latitude;
    private String longitude;
    private String name;
    private String nearbySpot;
    private String net;
    private int pid;
    private String price;
    private String restDay;
    private String rgb;
    private int suggest;
    private String tel;
    private String traffic;
    private int width;
    /**
     * key : 美食
     * value : 89
     */

    private List<GenesEntity> genes;
    /**
     * zh : 现金
     */

    private List<IconsEntity> icons;
    /**
     * cid : 86
     * cindex : 0
     * height : 605
     * imgpath : http://7xo7et.com1.z0.glb.clouddn.com/4@1?imageMogr2/format/webp
     * width : 909
     */

    private List<ImglistEntity> imglist;
    /**
     * zh : 下午茶
     */

    private List<TagsEntity> tags;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setIsf(boolean isf) {
        this.isf = isf;
    }

    public void setIsout(boolean isout) {
        this.isout = isout;
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

    public void setNearbySpot(String nearbySpot) {
        this.nearbySpot = nearbySpot;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRestDay(String restDay) {
        this.restDay = restDay;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public void setSuggest(int suggest) {
        this.suggest = suggest;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setGenes(List<GenesEntity> genes) {
        this.genes = genes;
    }

    public void setIcons(List<IconsEntity> icons) {
        this.icons = icons;
    }

    public void setImglist(List<ImglistEntity> imglist) {
        this.imglist = imglist;
    }

    public void setTags(List<TagsEntity> tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public String getCover() {
        return cover;
    }

    public int getHeight() {
        return height;
    }

    public boolean isIsf() {
        return isf;
    }

    public boolean isIsout() {
        return isout;
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

    public String getNearbySpot() {
        return nearbySpot;
    }

    public String getNet() {
        return net;
    }

    public int getPid() {
        return pid;
    }

    public String getPrice() {
        return price;
    }

    public String getRestDay() {
        return restDay;
    }

    public String getRgb() {
        return rgb;
    }

    public int getSuggest() {
        return suggest;
    }

    public String getTel() {
        return tel;
    }

    public String getTraffic() {
        return traffic;
    }

    public int getWidth() {
        return width;
    }

    public List<GenesEntity> getGenes() {
        return genes;
    }

    public List<IconsEntity> getIcons() {
        return icons;
    }

    public List<ImglistEntity> getImglist() {
        return imglist;
    }

    public List<TagsEntity> getTags() {
        return tags;
    }

    public static class GenesEntity {
        private String key;
        private String value;

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public static class IconsEntity {
        private String zh;

        public void setZh(String zh) {
            this.zh = zh;
        }

        public String getZh() {
            return zh;
        }
    }

    public static class ImglistEntity {
        private int cid;
        private int cindex;
        private int height;
        private String imgpath;
        private int width;

        public void setCid(int cid) {
            this.cid = cid;
        }

        public void setCindex(int cindex) {
            this.cindex = cindex;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setImgpath(String imgpath) {
            this.imgpath = imgpath;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getCid() {
            return cid;
        }

        public int getCindex() {
            return cindex;
        }

        public int getHeight() {
            return height;
        }

        public String getImgpath() {
            return imgpath;
        }

        public int getWidth() {
            return width;
        }
    }

    public static class TagsEntity {
        private String zh;

        public void setZh(String zh) {
            this.zh = zh;
        }

        public String getZh() {
            return zh;
        }
    }
}
