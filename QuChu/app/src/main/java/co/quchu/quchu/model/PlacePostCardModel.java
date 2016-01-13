package co.quchu.quchu.model;

import java.io.Serializable;
import java.util.List;

/**
 * PlacePostCardModel
 * User: Chenhs
 * Date: 2015-12-18
 */
public class PlacePostCardModel implements Serializable {

    /**
     * ishave : true
     * page : {"pageCount":1,"pagesNo":1,"result":[{"address":"华新路32号","autor":"chslalalala","autorId":11,"autorPhoto":"http://tp1.sinaimg.cn/5230113944/50/40061324886/1","cardId":16,"comment":"zge chg CFS出的广告牌坊下的女人俱乐部位子弹","favoNum":0,"height":520,"imglist":[{"imgId":469,"isCover":true,"path":"http://7xo7et.com1.z0.glb.clouddn.com/11_1450428218588.JPEG?imageMogr2/format/webp"}],"isf":false,"isme":true,"isp":false,"placeId":3,"plcaeAddress":"厦门,中山路/轮渡","plcaeCover":"http://7xo7et.com1.z0.glb.clouddn.com/11_1450428218588.JPEG?imageMogr2/format/webp","plcaeName":"32 How","praiseNum":0,"rgb":"6a6763","score":5,"tel":"0592-2916511","time":"2015-12-18 04:45:03","width":390}]}
     */

    private boolean ishave;
    /**
     * pageCount : 1
     * pagesNo : 1
     * result : [{"address":"华新路32号","autor":"chslalalala","autorId":11,"autorPhoto":"http://tp1.sinaimg.cn/5230113944/50/40061324886/1","cardId":16,"comment":"zge chg CFS出的广告牌坊下的女人俱乐部位子弹","favoNum":0,"height":520,"imglist":[{"imgId":469,"isCover":true,"path":"http://7xo7et.com1.z0.glb.clouddn.com/11_1450428218588.JPEG?imageMogr2/format/webp"}],"isf":false,"isme":true,"isp":false,"placeId":3,"plcaeAddress":"厦门,中山路/轮渡","plcaeCover":"http://7xo7et.com1.z0.glb.clouddn.com/11_1450428218588.JPEG?imageMogr2/format/webp","plcaeName":"32 How","praiseNum":0,"rgb":"6a6763","score":5,"tel":"0592-2916511","time":"2015-12-18 04:45:03","width":390}]
     */

    private PageEntity page;

    public void setIshave(boolean ishave) {
        this.ishave = ishave;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    public boolean isIshave() {
        return ishave;
    }

    public PageEntity getPage() {
        return page;
    }

    public static class PageEntity implements Serializable{
        private int pageCount;
        private int pagesNo;
        /**
         * address : 华新路32号
         * autor : chslalalala
         * autorId : 11
         * autorPhoto : http://tp1.sinaimg.cn/5230113944/50/40061324886/1
         * cardId : 16
         * comment : zge chg CFS出的广告牌坊下的女人俱乐部位子弹
         * favoNum : 0
         * height : 520
         * imglist : [{"imgId":469,"isCover":true,"path":"http://7xo7et.com1.z0.glb.clouddn.com/11_1450428218588.JPEG?imageMogr2/format/webp"}]
         * isf : false
         * isme : true
         * isp : false
         * placeId : 3
         * plcaeAddress : 厦门,中山路/轮渡
         * plcaeCover : http://7xo7et.com1.z0.glb.clouddn.com/11_1450428218588.JPEG?imageMogr2/format/webp
         * plcaeName : 32 How
         * praiseNum : 0
         * rgb : 6a6763
         * score : 5
         * tel : 0592-2916511
         * time : 2015-12-18 04:45:03
         * width : 390
         */

        private List<PostCardItemModel> result;

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public void setPagesNo(int pagesNo) {
            this.pagesNo = pagesNo;
        }

        public void setResult(List<PostCardItemModel> result) {
            this.result = result;
        }

        public int getPageCount() {
            return pageCount;
        }

        public int getPagesNo() {
            return pagesNo;
        }

        public List<PostCardItemModel> getResult() {
            return result;
        }


    }
}
