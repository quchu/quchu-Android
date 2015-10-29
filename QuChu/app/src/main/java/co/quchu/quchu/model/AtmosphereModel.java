package co.quchu.quchu.model;

import java.util.List;

/**
 * AtmosphereModel
 * User: Chenhs
 * Date: 2015-10-29
 * 氛围列表item model
 */
public class AtmosphereModel {

    /**
     * pageCount : 19
     * pageSize : 10
     * pagesNo : 1
     * result : [{"address":"思明区华新路13号花园别墅(近中山公园西门)","cover":"http://imgdn.paimeilv.com/7-default-place-cover","describe":"厦门,鼓浪屿","height":639,"latitude":"24.465574","longitude":"118.093499","name":"不在书店","pid":7,"rgb":"84785c","takeIndex":5,"width":960}]
     * resultCount : 10
     * rowCount : 183
     * rowEnd : 10
     * rowStart : 0
     */

    private DataEntity data;
    /**
     * data : {"pageCount":19,"pageSize":10,"pagesNo":1,"result":[{"address":"思明区华新路13号花园别墅(近中山公园西门)","cover":"http://imgdn.paimeilv.com/7-default-place-cover","describe":"厦门,鼓浪屿","height":639,"latitude":"24.465574","longitude":"118.093499","name":"不在书店","pid":7,"rgb":"84785c","takeIndex":5,"width":960}],"resultCount":10,"rowCount":183,"rowEnd":10,"rowStart":0}
     * exception :
     * msg : success
     * result : true
     */

    private String exception;
    private String msg;
    private boolean result;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public DataEntity getData() {
        return data;
    }

    public String getException() {
        return exception;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isResult() {
        return result;
    }

    public static class DataEntity {
        private int pageCount;
        private int pageSize;
        private int pagesNo;
        private int resultCount;
        private int rowCount;
        private int rowEnd;
        private int rowStart;
        /**
         * address : 思明区华新路13号花园别墅(近中山公园西门)
         * cover : http://imgdn.paimeilv.com/7-default-place-cover
         * describe : 厦门,鼓浪屿
         * height : 639
         * latitude : 24.465574
         * longitude : 118.093499
         * name : 不在书店
         * pid : 7
         * rgb : 84785c
         * takeIndex : 5.0
         * width : 960
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

        public void setResult(List<ResultEntity> result) {
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

        public List<ResultEntity> getResult() {
            return result;
        }

        public static class ResultEntity {
            private String address;
            private String cover;
            private String describe;
            private int height;
            private String latitude;
            private String longitude;
            private String name;
            private int pid;
            private String rgb;
            private double takeIndex;
            private int width;

            public void setAddress(String address) {
                this.address = address;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
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

            public void setTakeIndex(double takeIndex) {
                this.takeIndex = takeIndex;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getAddress() {
                return address;
            }

            public String getCover() {
                return cover;
            }

            public String getDescribe() {
                return describe;
            }

            public int getHeight() {
                return height;
            }

            public String getLatitude() {
                return latitude;
            }

            public String getLongitude() {
                return longitude;
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

            public double getTakeIndex() {
                return takeIndex;
            }

            public int getWidth() {
                return width;
            }
        }
    }
}
