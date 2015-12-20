package co.quchu.quchu.model;

import java.util.List;

/**
 * PostCardDetailModel
 * User: Chenhs
 * Date: 2015-12-20
 * 明信片详情 model
 */
public class PostCardDetailModel {

    /**
     * address : 鹭江道3号和平码头当丰新天地3楼302室
     * autor : chslalalala
     * autorId : 11
     * autorPhoto : http://tp1.sinaimg.cn/5230113944/50/40061324886/1
     * cardId : 17
     * comment : 覆盖才不会if的
     * favoNum : 1
     * imglist : [{"height":520,"imgId":482,"isCover":true,"path":"http://7xo7et.com1.z0.glb.clouddn.com/11_1450435086650.JPEG?imageMogr2/format/webp","rgb":"4d5156","width":390}]
     * isf : false
     * isme : false
     * isp : false
     * placeId : 4
     * plcaeAddress : 厦门,中山路/轮渡
     * plcaeCover : http://7xo7et.com1.z0.glb.clouddn.com/11_1450435086650.JPEG?imageMogr2/format/webp
     * plcaeName : ReNext里遇
     * praiseNum : 0
     * rgb : 4d5156
     * score : 5
     * tel : 0592-2233995
     * time : 2015-12-18 06:37:50
     */

    private String address;
    private String autor;
    private int autorId;
    private String autorPhoto;
    private int cardId;
    private String comment="";
    private int favoNum;
    private boolean isf;
    private boolean isme;
    private boolean isp;
    private int placeId;
    private String plcaeAddress="";
    private String plcaeCover="";
    private String plcaeName="";
    private int praiseNum;
    private String rgb;
    private int score;
    private String tel;
    private String time;
    /**
     * height : 520
     * imgId : 482
     * isCover : true
     * path : http://7xo7et.com1.z0.glb.clouddn.com/11_1450435086650.JPEG?imageMogr2/format/webp
     * rgb : 4d5156
     * width : 390
     */

    private List<ImglistEntity> imglist;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
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

    public void setIsf(boolean isf) {
        this.isf = isf;
    }

    public void setIsme(boolean isme) {
        this.isme = isme;
    }

    public void setIsp(boolean isp) {
        this.isp = isp;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
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

    public void setImglist(List<ImglistEntity> imglist) {
        this.imglist = imglist;
    }

    public String getAddress() {
        return address;
    }

    public String getAutor() {
        return autor;
    }

    public int getAutorId() {
        return autorId;
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

    public boolean isIsf() {
        return isf;
    }

    public boolean isIsme() {
        return isme;
    }

    public boolean isIsp() {
        return isp;
    }

    public int getPlaceId() {
        return placeId;
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
