package co.quchu.quchu.model;

import java.util.ArrayList;

/**
 * FlickrModel
 * User: Chenhs
 * Date: 2015-11-20
 * 网络相册model
 */
public class FlickrModel {

    /**
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/1442483892452
     * height : 292
     * num : 5
     * rgb : 827d63
     * width : 433
     */
    private FavoriteEntity favorite;
    /**
     * pageSize : 5
     * pagesNo : 1
     * result : [{"height":683,"imagId":45,"path":"http://7xo7et.com1.z0.glb.clouddn.com/1442461307213","rgb":"606769","width":1024}]
     */

    private ImgsEntity imgs;
    /**
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/1442459807995
     * height : 639
     * num : 594
     * width : 960
     */

    private PhotoEntity photo;

    public void setFavorite(FavoriteEntity favorite) {
        this.favorite = favorite;
    }

    public void setImgs(ImgsEntity imgs) {
        this.imgs = imgs;
    }

    public void setPhoto(PhotoEntity photo) {
        this.photo = photo;
    }

    public FavoriteEntity getFavorite() {
        return favorite;
    }

    public ImgsEntity getImgs() {
        return imgs;
    }

    public PhotoEntity getPhoto() {
        return photo;
    }

    public static class FavoriteEntity {
        private String cover;
        private int num;

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getCover() {
            return cover;
        }

        public int getNum() {
            return num;
        }
    }

    public static class ImgsEntity {
        private int pageSize;
        private int pagesNo;
        /**
         * height : 683
         * imagId : 45
         * path : http://7xo7et.com1.z0.glb.clouddn.com/1442461307213
         * rgb : 606769
         * width : 1024
         */

        private ArrayList<String> photos;

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setPagesNo(int pagesNo) {
            this.pagesNo = pagesNo;
        }


        public int getPageSize() {
            return pageSize;
        }

        public int getPagesNo() {
            return pagesNo;
        }

        public ArrayList<String> getPhotos() {
            return photos;
        }

        public void setPhoto(ArrayList<String> list) {
            this.photos=list;
        }
    }

    public static class PhotoEntity {
        private String cover;
        private int num;

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getCover() {
            return cover;
        }

        public int getNum() {
            return num;
        }
    }
}
