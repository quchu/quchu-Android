package co.quchu.quchu.model;

import java.util.List;

/**
 * RecommendModel
 * User: Chenhs
 * Date: 2015-12-08
 */
public class RecommendModelNew {


    /**
     * pageCount : 1
     * pageSize : 10
     * pagesNo : 1
     * result : [{"address":"嘉禾路197-199号磐基中心之名品中心L1层125号","cover":"http://7xo7et.com1.z0.glb.clouddn.com/2-default-app-place-cover","describe":"厦门","distance":"3.7500391125203367","genes":[{"value":"95","key":"文艺"},{"value":"99","key":"氛围"},{"value":"60","key":"美食"},{"value":"80","key":"主题"}],"height":935,"latitude":"24.488348210734","longitude":"118.12737204738","name":"Monceau Lifestyle 茉颂","pid":2,"rgb":"725a60","suggest":null,"width":666,"\u201cisActivity\u201d":false}]
     * resultCount : 9
     * rowCount : null
     * rowEnd : null
     * rowStart : 0
     */

    private int pageCount;
    private int pageSize;
    private int pagesNo;
    private int resultCount;
    private Object rowCount;
    private Object rowEnd;
    private int rowStart;
    /**
     * address : 嘉禾路197-199号磐基中心之名品中心L1层125号
     * cover : http://7xo7et.com1.z0.glb.clouddn.com/2-default-app-place-cover
     * describe : 厦门
     * distance : 3.7500391125203367
     * genes : [{"value":"95","key":"文艺"},{"value":"99","key":"氛围"},{"value":"60","key":"美食"},{"value":"80","key":"主题"}]
     * height : 935
     * latitude : 24.488348210734
     * longitude : 118.12737204738
     * name : Monceau Lifestyle 茉颂
     * pid : 2
     * rgb : 725a60
     * suggest : null
     * width : 666
     * “isActivity” : false
     */

    private List<RecommendModel> result;

    public List<RecommendModel> getResult() {
        return result;
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

    public Object getRowCount() {
        return rowCount;
    }

    public Object getRowEnd() {
        return rowEnd;
    }

    public int getRowStart() {
        return rowStart;
    }
}
