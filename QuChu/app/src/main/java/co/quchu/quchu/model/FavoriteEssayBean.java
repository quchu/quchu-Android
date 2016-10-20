package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by no21 on 2016/6/30.
 * email:437943145@qq.com
 * desc :
 */
public class FavoriteEssayBean {
    /**
     * pageCount : 1
     * pageSize : 10
     * pagesNo : 1
     * result : [{"articleId":1,"articleName":"测试","articleComtent":"sdsdsf","articleCreateUserId":1,"imageId":200,"readCount":0,"imageUrl":"http://7vzrp0.com5.z0.glb.clouddn.com/scene/scene_1.jpg","favoriteCount":0}]
     * resultCount : 1
     * rowCount : 1
     * rowEnd : 1
     * rowStart : 0
     */

    private int pageCount;
    private int pageSize;
    private int pagesNo;
    private int resultCount;
    private int rowCount;
    private int rowEnd;
    private int rowStart;
    /**
     * articleId : 1
     * articleName : 测试
     * articleComtent : sdsdsf
     * articleCreateUserId : 1
     * imageId : 200
     * readCount : 0
     * imageUrl : http://7vzrp0.com5.z0.glb.clouddn.com/scene/scene_1.jpg
     * favoriteCount : 0
     */

    private List<ResultBean> result;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPagesNo() {
        return pagesNo;
    }

    public void setPagesNo(int pagesNo) {
        this.pagesNo = pagesNo;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getRowEnd() {
        return rowEnd;
    }

    public void setRowEnd(int rowEnd) {
        this.rowEnd = rowEnd;
    }

    public int getRowStart() {
        return rowStart;
    }

    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int articleId;
        private String articleName;
        private String articleComtent;
        private int articleCreateUserId;
        private int imageId;
        private int readCount;
        private String imageUrl;
        private int favoriteCount;
        private String autorPhoto;
        private String name;
        private String autor;

        public String getAutor() {
            return autor;
        }

        public void setAutor(String autor) {
            this.autor = autor;
        }

        public String getAutorPhoto() {
            return autorPhoto;
        }

        public void setAutorPhoto(String autorPhoto) {
            this.autorPhoto = autorPhoto;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getArticleId() {
            return articleId;
        }

        public void setArticleId(int articleId) {
            this.articleId = articleId;
        }

        public String getArticleName() {
            return articleName;
        }

        public void setArticleName(String articleName) {
            this.articleName = articleName;
        }

        public String getArticleComtent() {
            return articleComtent;
        }

        public void setArticleComtent(String articleComtent) {
            this.articleComtent = articleComtent;
        }

        public int getArticleCreateUserId() {
            return articleCreateUserId;
        }

        public void setArticleCreateUserId(int articleCreateUserId) {
            this.articleCreateUserId = articleCreateUserId;
        }

        public int getImageId() {
            return imageId;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

        public int getReadCount() {
            return readCount;
        }

        public void setReadCount(int readCount) {
            this.readCount = readCount;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getFavoriteCount() {
            return favoriteCount;
        }

        public void setFavoriteCount(int favoriteCount) {
            this.favoriteCount = favoriteCount;
        }

        @Override public String toString() {
            return "ResultBean{" +
                "articleId=" + articleId +
                ", articleName='" + articleName + '\'' +
                ", articleComtent='" + articleComtent + '\'' +
                ", articleCreateUserId=" + articleCreateUserId +
                ", imageId=" + imageId +
                ", readCount=" + readCount +
                ", imageUrl='" + imageUrl + '\'' +
                ", favoriteCount=" + favoriteCount +
                ", autorPhoto='" + autorPhoto + '\'' +
                ", name='" + name + '\'' +
                ", autor='" + autor + '\'' +
                '}';
        }
    }

}
