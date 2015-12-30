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

    private List<PostCardItemModel> result;

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

    public void setResult(List<PostCardItemModel> result) {
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

    public List<PostCardItemModel> getResult() {
        return result;
    }


}
