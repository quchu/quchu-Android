package co.quchu.quchu.model;

/**
 * ClassifyModel
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处分类 model
 */
public class ClassifyModel {

    /**
     * en : architecture
     * isSend : false
     * maxImg : http://7vzrp0.com5.z0.glb.clouddn.com/architecture-max
     * minImg : http://7vzrp0.com5.z0.glb.clouddn.com/architecture-min
     * weight : 0
     * zh : 砖瓦之间
     */

    private String en;
    private boolean isSend = true;
    private String maxImg;
    private String minImg;
    private double weight;
    private String zh;

    public void setEn(String en) {
        this.en = en;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    public void setMaxImg(String maxImg) {
        this.maxImg = maxImg;
    }

    public void setMinImg(String minImg) {
        this.minImg = minImg;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getEn() {
        return en;
    }

    public boolean isIsSend() {
        return isSend;
    }

    public String getMaxImg() {
        return maxImg;
    }

    public String getMinImg() {
        return minImg;
    }

    public double getWeight() {
        return weight;
    }

    public String getZh() {
        return zh;
    }
}
