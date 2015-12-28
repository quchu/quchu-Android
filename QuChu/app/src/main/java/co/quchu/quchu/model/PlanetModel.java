package co.quchu.quchu.model;

import java.util.List;

/**
 * PlanetModel
 * User: Chenhs
 * Date: 2015-12-28
 */
public class PlanetModel {
    /**
     * cardNum : 2
     * followNum : 0
     * fovNum : 4
     * hostNum : 0
     * imgNum : 11
     * imgs : [{"height":1040,"imagId":657,"path":"http://7xo7et.com1.z0.glb.clouddn.com/13_1450782506871.JPEG?imageMogr2/format/webp","rgb":"52514f","width":780}]
     * proposalNum : 9
     * star : [{"en":"creative","maxImg":"http://7vzrp0.com5.z0.glb.clouddn.com/creative-max","minImg":"http://7vzrp0.com5.z0.glb.clouddn.com/creative-min","weight":57.43162901,"zh":"创意"}]
     */
    private int cardNum;
    private int followNum;
    private int fovNum;
    private int hostNum;
    private int imgNum;
    private int proposalNum;
    /**
     * height : 1040
     * imagId : 657
     * path : http://7xo7et.com1.z0.glb.clouddn.com/13_1450782506871.JPEG?imageMogr2/format/webp
     * rgb : 52514f
     * width : 780
     */

    private List<ImgsEntity> imgs;
    /**
     * en : creative
     * maxImg : http://7vzrp0.com5.z0.glb.clouddn.com/creative-max
     * minImg : http://7vzrp0.com5.z0.glb.clouddn.com/creative-min
     * weight : 57.43162901
     * zh : 创意
     */

    private List<StarEntity> star;

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public void setFovNum(int fovNum) {
        this.fovNum = fovNum;
    }

    public void setHostNum(int hostNum) {
        this.hostNum = hostNum;
    }

    public void setImgNum(int imgNum) {
        this.imgNum = imgNum;
    }

    public void setProposalNum(int proposalNum) {
        this.proposalNum = proposalNum;
    }

    public void setImgs(List<ImgsEntity> imgs) {
        this.imgs = imgs;
    }

    public void setStar(List<StarEntity> star) {
        this.star = star;
    }

    public int getCardNum() {
        return cardNum;
    }

    public int getFollowNum() {
        return followNum;
    }

    public int getFovNum() {
        return fovNum;
    }

    public int getHostNum() {
        return hostNum;
    }

    public int getImgNum() {
        return imgNum;
    }

    public int getProposalNum() {
        return proposalNum;
    }

    public List<ImgsEntity> getImgs() {
        return imgs;
    }

    public List<StarEntity> getStar() {
        return star;
    }

    public static class ImgsEntity {
        private int height;
        private int imagId;
        private String path;
        private String rgb;
        private int width;

        public void setHeight(int height) {
            this.height = height;
        }

        public void setImagId(int imagId) {
            this.imagId = imagId;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setRgb(String rgb) {
            this.rgb = rgb;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public int getImagId() {
            return imagId;
        }

        public String getPath() {
            return path;
        }

        public String getRgb() {
            return rgb;
        }

        public int getWidth() {
            return width;
        }
    }

    public static class StarEntity {
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
