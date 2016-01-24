package co.quchu.quchu.model;

import java.util.List;

/**
 * FavoritePlaceModel
 * User: Chenhs
 * Date: 2015-12-15
 * 我收藏的趣处 model
 */
public class FavoritePlaceModel {

    /**
     * address : 厦门市开元路116号
     * autor : 管理员
     * autorId : 1
     * autorPhoto : http://7xo7et.com1.z0.glb.clouddn.com/1-app-default-avatar
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/7-default-app-place-cover?imageMogr2/format/webp
     * describe : 厦门,中山路/轮渡
     * height : 533
     * isf : true
     * latitude : 24.463229666615
     * longitude : 118.08169053347
     * name : 吉治百货
     * pid : 7
     * rgb : 7f827a
     * suggest : 5
     * width : 800
     */

    private List<ResultEntity> result;

    public void setResult(List<ResultEntity> result) {
        this.result = result;
    }

    public List<ResultEntity> getResult() {
        return result;
    }

    public static class ResultEntity {
        private String address;
        private String autor;
        private String autorId;
        private String autorPhoto;
        private String cover;
        private String describe;
        private boolean isf;
        private String name;
        private int pid;
        private String rgb;
        private float suggest;

        public void setAddress(String address) {
            this.address = address;
        }

        public void setAutor(String autor) {
            this.autor = autor;
        }

        public void setAutorId(String autorId) {
            this.autorId = autorId;
        }

        public void setAutorPhoto(String autorPhoto) {
            this.autorPhoto = autorPhoto;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public void setIsf(boolean isf) {
            this.isf = isf;
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

        public void setSuggest(float suggest) {
            this.suggest = suggest;
        }

        public String getAddress() {
            return address;
        }

        public String getAutor() {
            return autor;
        }

        public String getAutorId() {
            return autorId;
        }

        public String getAutorPhoto() {
            return autorPhoto;
        }

        public String getCover() {
            return cover;
        }

        public String getDescribe() {
            return describe;
        }

        public boolean isIsf() {
            return isf;
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

        public float getSuggest() {
            return suggest;
        }
    }
}
