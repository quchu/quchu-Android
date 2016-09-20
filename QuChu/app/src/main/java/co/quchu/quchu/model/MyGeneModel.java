package co.quchu.quchu.model;

import java.io.Serializable;
import java.util.List;

/**
 * MyGeneModel
 * User: Chenhs
 * Date: 2015-12-28
 */
public class MyGeneModel implements Serializable {


    /**
     * en : curiosity
     * weight : 34.934963636363634
     * zh : 好奇
     */

    private List<GenesEntity> genes;
    /**
     * en : creative
     * maxImg : http://7vzrp0.com5.z0.glb.clouddn.com/creative-max
     * minImg : http://7vzrp0.com5.z0.glb.clouddn.com/creative-min
     * weight : 58.80322209
     * zh : 创意
     */

    private List<StarEntity> star;

    public void setGenes(List<GenesEntity> genes) {
        this.genes = genes;
    }

    public void setStar(List<StarEntity> star) {
        this.star = star;
    }

    public List<GenesEntity> getGenes() {
        return genes;
    }

    public List<StarEntity> getStar() {
        return star;
    }

    public static class GenesEntity implements Serializable {
        private String en;
        private double weight;
        private String zh;
        private String mark;

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public void setEn(String en) {
            this.en = en;
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

        public double getWeight() {
            return weight;
        }

        public String getZh() {
            return zh;
        }
    }

    public static class StarEntity implements Serializable {
        private String en;
        private String maxImg;
        private String minImg;
        private double weight;
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

        public void setWeight(double weight) {
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

        public double getWeight() {
            return weight;
        }

        public String getZh() {
            return zh;
        }
    }
}
