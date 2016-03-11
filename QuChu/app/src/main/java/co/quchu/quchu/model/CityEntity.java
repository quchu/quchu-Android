package co.quchu.quchu.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linqipeng on 2016/3/11 11:22
 * email:437943145@qq.com
 * desc:
 */
public class CityEntity {

    /**
     * cvalue : 厦门
     * cid : 5
     */

    @SerializedName("default")
    private DefaultEntity defaultX;
    /**
     * pageCount : 1
     * pageSize : 10
     * pagesNo : 1
     * result : [{"cvalue":"厦门","cid":5},{"cvalue":"杭州","cid":6},{"cvalue":"台北","cid":10},{"cvalue":"南京","cid":11}]
     * resultCount : 4
     * rowCount : 4
     * rowEnd : 4
     * rowStart : 0
     */

    private PageEntity page;

    public void setDefaultX(DefaultEntity defaultX) {
        this.defaultX = defaultX;
    }

    public void setPage(PageEntity page) {
        this.page = page;
    }

    public DefaultEntity getDefaultX() {
        return defaultX;
    }

    public PageEntity getPage() {
        return page;
    }

    public static class DefaultEntity {
        private String cvalue;
        private int cid;

        public void setCvalue(String cvalue) {
            this.cvalue = cvalue;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getCvalue() {
            return cvalue;
        }

        public int getCid() {
            return cid;
        }
    }

    public static class PageEntity {
        private int pageCount;
        private int pageSize;
        private int pagesNo;
        private int resultCount;
        private int rowCount;
        private int rowEnd;
        private int rowStart;
        /**
         * cvalue : 厦门
         * cid : 5
         */

        private ArrayList<CityModel> result;

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

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public void setRowEnd(int rowEnd) {
            this.rowEnd = rowEnd;
        }

        public void setRowStart(int rowStart) {
            this.rowStart = rowStart;
        }

        public void setResult(ArrayList<CityModel> result) {
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

        public int getRowCount() {
            return rowCount;
        }

        public int getRowEnd() {
            return rowEnd;
        }

        public int getRowStart() {
            return rowStart;
        }

        public ArrayList<CityModel> getResult() {
            return result;
        }

        public static class ResultEntity {
            private String cvalue;
            private int cid;

            public void setCvalue(String cvalue) {
                this.cvalue = cvalue;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }

            public String getCvalue() {
                return cvalue;
            }

            public int getCid() {
                return cid;
            }
        }
    }
}
