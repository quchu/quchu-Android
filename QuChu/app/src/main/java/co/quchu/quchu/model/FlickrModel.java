package co.quchu.quchu.model;

import java.util.List;

/**
 * FlickrModel
 * User: Chenhs
 * Date: 2015-11-20
 * 网络相册model
 */
public class FlickrModel {

    /**
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/1442483892452
     * num : 5
     */

    private FavoriteEntity favorite;
    /**
     * pageCount : 119
     * pageSize : 5
     * pagesNo : 1
     * result : [{"imagId":45,"path":"http://7xo7et.com1.z0.glb.clouddn.com/1442461307213"}]
     */

    private ImgsEntity imgs;
    /**
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/1442460151639
     * num : 594
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
        private int pageCount;
        private int pageSize;
        private int pagesNo;
        /**
         * imagId : 45
         * path : http://7xo7et.com1.z0.glb.clouddn.com/1442461307213
         */

        private List<ResultEntity> result;

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setPagesNo(int pagesNo) {
            this.pagesNo = pagesNo;
        }

        public void setResult(List<ResultEntity> result) {
            this.result = result;
        }
        public void addResult(List<ResultEntity> result) {
            this.result.addAll(result);
        }

        public int getPageCount() {
            return pageCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getPagesNo() {
            return pagesNo;
        }

        public List<ResultEntity> getResult() {
            return result;
        }

        public static class ResultEntity {
            private int imagId;
            private String path;

            public void setImagId(int imagId) {
                this.imagId = imagId;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public int getImagId() {
                return imagId;
            }

            public String getPath() {
                return path;
            }
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
