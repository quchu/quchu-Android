package co.quchu.quchu.model;

import java.util.List;

/**
 * RecommendModel
 * User: Chenhs
 * Date: 2015-12-08
 */
public class RecommendModel {
    /**
     * address : 仙岳路8号桥头堡文创园内
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/20-default-app-place-cover?imageMogr2/format/webp
     * describe : 厦门
     * distance : 26060.471714041065
     * genes : [{"key":"夜生活","value":"90"}]
     * height : 683
     * isf : false
     * latitude : 24.497263415041
     * longitude : 118.09703816664
     * name : 迷待音乐会所
     * pid : 20
     * rgb : b7a867
     * suggest : 4
     * tags : []
     * width : 1024
     */

    private String address;
    private String cover;
    private String describe;
    private String distance;
    private int height;
    private boolean isf;
    private String latitude;
    private String longitude;
    private String name;
    private int pid;
    private String rgb;
    private int suggest;
    private int width;
    /**
     * key : 夜生活
     * value : 90
     */

    private List<GenesEntity> genes;
    private List<?> tags;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setIsf(boolean isf) {
        this.isf = isf;
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

    public void setSuggest(int suggest) {
        this.suggest = suggest;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setGenes(List<GenesEntity> genes) {
        this.genes = genes;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
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

    public String getDistance() {
        return distance;
    }

    public int getHeight() {
        return height;
    }

    public boolean isIsf() {
        return isf;
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

    public int getSuggest() {
        return suggest;
    }

    public int getWidth() {
        return width;
    }

    public List<GenesEntity> getGenes() {
        return genes;
    }

    public List<?> getTags() {
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

   /* *//**
     * address : 嘉禾路197-199号磐基中心之名品中心L1层125号
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/2-default-app-place-cover
     * describe : 厦门
     * distance : 3.7500391125203367
     * genes : [{"key":"文艺","value":"95"}]
     * latitude : 24.488348210734
     * longitude : 118.12737204738
     * name : Monceau Lifestyle 茉颂
     * pid : 2
     * rgb : 725a60
     * tags : [{"zh":"概念生活馆"}]
     *//*

    private String address;
    private String cover;
    private String describe;
    private String distance;
    private String latitude;
    private String longitude;
    private String name;
    private int pid;
    private String rgb;
    private double suggest = 5;
    *//**
     * key : 文艺
     * value : 95
     *//*

    private List<GenesEntity> genes;
    *//**
     * zh : 概念生活馆
     *//*
    private List<TagsEntity> tags;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public void setGenes(List<GenesEntity> genes) {
        this.genes = genes;
    }

    public void setTags(List<TagsEntity> tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public String getCover() {
        return cover;
    }

    public void setSuggest(double suggest) {
        this.suggest = suggest;
    }

    public double getSuggess() {
        return suggest;
    }

    public String getDescribe() {
        return describe;
    }

    public String getDistance() {
        return distance;
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

    public List<GenesEntity> getGenes() {
        return genes;
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

    public static class TagsEntity {
        private String zh;

        public void setZh(String zh) {
            this.zh = zh;
        }

        public String getZh() {
            return zh;
        }
    }*/

}