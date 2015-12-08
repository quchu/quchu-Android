package co.quchu.quchu.model;

/**
 * ClassifyModel
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处分类 model
 */
public class ClassifyModel {

    /**
     * en : luxury
     * maxImg : http://7vzrp0.com5.z0.glb.clouddn.com/luxury-max
     * minImg : http://7vzrp0.com5.z0.glb.clouddn.com/luxury-min
     * weight : 0
     * zh : 轻奢
     */

    private String en;
    private String maxImg;
    private String minImg;
    private int weight;
    private String zh;

    public void setEn(String en) {
        this.en = en;
    }

    public void setMaxImg(String maxImg) {
        this.maxImg = maxImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getEn() {
        return en;
    }

    public String getMaxImg() {
        return maxImg;
    }

    public String getMinImg() {
        return minImg;
    }

    public int getWeight() {
        return weight;
    }

    public String getZh() {
        return zh;
    }
}
