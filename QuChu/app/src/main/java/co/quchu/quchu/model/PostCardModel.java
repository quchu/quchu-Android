package co.quchu.quchu.model;

import java.io.Serializable;
import java.util.List;

/**
 * PostCardModel
 * User: Chenhs
 * Date: 2015-11-12
 */
public class PostCardModel implements Serializable {
    /**
     * pageCount : 1
     * pageSize : 10
     * pagesNo : 1
     * result : [{"address":"厦门","autor":"趣处","autorPhoto":"http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-32","cardId":1,"comment":"╰(*°▽°*)╯欢迎登录趣星球~，准备开启你的趣星球之旅吧","favoNum":0,"height":800,"imglist":[{"height":1920,"imgId":109,"isCover":false,"path":"http://7xo7et.com1.z0.glb.clouddn.com/1449473965496?imageMogr2/format/webp","rgb":"625f58","width":1080}],"isf":false,"isp":false,"issys":true,"plcaeAddress":"宇宙","plcaeCover":"http://7xo7et.com1.z0.glb.clouddn.com/1449473969979?imageMogr2/format/webp","plcaeName":"趣处","praiseNum":0,"rgb":"676355","score":5,"tel":"","time":"2015-12-07 03:38:02","width":980}]
     * resultCount : 1
     * rowStart : 0
     */

    private int pageCount;
    private int pageSize;
    private int pagesNo;
    private int resultCount;
    private int rowStart;
    /**
     * address : 厦门
     * autor : 趣处
     * autorPhoto : http://7xo7et.com1.z0.glb.clouddn.com/app-default-avatar-32
     * cardId : 1
     * comment : ╰(*°▽°*)╯欢迎登录趣星球~，准备开启你的趣星球之旅吧
     * favoNum : 0
     * height : 800
     * imglist : [{"height":1920,"imgId":109,"isCover":false,"path":"http://7xo7et.com1.z0.glb.clouddn.com/1449473965496?imageMogr2/format/webp","rgb":"625f58","width":1080}]
     * isf : false
     * isp : false
     * issys : true
     * plcaeAddress : 宇宙
     * plcaeCover : http://7xo7et.com1.z0.glb.clouddn.com/1449473969979?imageMogr2/format/webp
     * plcaeName : 趣处
     * praiseNum : 0
     * rgb : 676355
     * score : 5
     * tel :
     * time : 2015-12-07 03:38:02
     * width : 980
     */

    private List<PostCardItem> result;

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPagesNo(int pagesNo) {
        this.pagesNo = pagesNo;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public void setRowStart(int rowStart) {
        this.rowStart = rowStart;
    }

    public void setResult(List<PostCardItem> result) {
        this.result = result;
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

    public int getResultCount() {
        return resultCount;
    }

    public int getRowStart() {
        return rowStart;
    }

    public List<PostCardItem> getResult() {
        return result;
    }

    public static class PostCardItem {
        private String address;
        private String autor;
        private String autorPhoto;
        private int cardId;
        private String comment;
        private int favoNum;
        private int height;
        private boolean isf;
        private boolean isp;
        private boolean issys;
        private String plcaeAddress;
        private String plcaeCover;
        private String plcaeName;
        private int praiseNum;
        private String rgb;
        private int score;
        private String tel;
        private String time;
        private int width;
        /**
         * height : 1920
         * imgId : 109
         * isCover : false
         * path : http://7xo7et.com1.z0.glb.clouddn.com/1449473965496?imageMogr2/format/webp
         * rgb : 625f58
         * width : 1080
         */

        private List<ImglistEntity> imglist;

        public void setAddress(String address) {
            this.address = address;
        }

        public void setAutor(String autor) {
            this.autor = autor;
        }

        public void setAutorPhoto(String autorPhoto) {
            this.autorPhoto = autorPhoto;
        }

        public void setCardId(int cardId) {
            this.cardId = cardId;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setFavoNum(int favoNum) {
            this.favoNum = favoNum;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setIsf(boolean isf) {
            this.isf = isf;
        }

        public void setIsp(boolean isp) {
            this.isp = isp;
        }

        public void setIssys(boolean issys) {
            this.issys = issys;
        }

        public void setPlcaeAddress(String plcaeAddress) {
            this.plcaeAddress = plcaeAddress;
        }

        public void setPlcaeCover(String plcaeCover) {
            this.plcaeCover = plcaeCover;
        }

        public void setPlcaeName(String plcaeName) {
            this.plcaeName = plcaeName;
        }

        public void setPraiseNum(int praiseNum) {
            this.praiseNum = praiseNum;
        }

        public void setRgb(String rgb) {
            this.rgb = rgb;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setImglist(List<ImglistEntity> imglist) {
            this.imglist = imglist;
        }

        public String getAddress() {
            return address;
        }

        public String getAutor() {
            return autor;
        }

        public String getAutorPhoto() {
            return autorPhoto;
        }

        public int getCardId() {
            return cardId;
        }

        public String getComment() {
            return comment;
        }

        public int getFavoNum() {
            return favoNum;
        }

        public int getHeight() {
            return height;
        }

        public boolean isIsf() {
            return isf;
        }

        public boolean isIsp() {
            return isp;
        }

        public boolean isIssys() {
            return issys;
        }

        public String getPlcaeAddress() {
            return plcaeAddress;
        }

        public String getPlcaeCover() {
            return plcaeCover;
        }

        public String getPlcaeName() {
            return plcaeName;
        }

        public int getPraiseNum() {
            return praiseNum;
        }

        public String getRgb() {
            return rgb;
        }

        public int getScore() {
            return score;
        }

        public String getTel() {
            return tel;
        }

        public String getTime() {
            return time;
        }

        public int getWidth() {
            return width;
        }

        public List<ImglistEntity> getImglist() {
            return imglist;
        }

        public static class ImglistEntity {
            private int height;
            private int imgId;
            private boolean isCover;
            private String path;
            private String rgb;
            private int width;

            public void setHeight(int height) {
                this.height = height;
            }

            public void setImgId(int imgId) {
                this.imgId = imgId;
            }

            public void setIsCover(boolean isCover) {
                this.isCover = isCover;
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

            public int getImgId() {
                return imgId;
            }

            public boolean isIsCover() {
                return isCover;
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
    }
}
