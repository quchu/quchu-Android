package co.quchu.quchu.model;

import java.util.List;

/**
 * ArticleListModel
 * User: Chenhs
 * Date: 2015-10-20
 */
public class ArticleListModel {

    /**
     * data : {"pageCount":1,"pageSize":5,"pagesNo":1,"result":[{"aImg":["http://imgdn.paimeilv.com/1444721523235","http://imgdn.paimeilv.com/1444721394094","http://imgdn.paimeilv.com/1442506776284","http://imgdn.paimeilv.com/1444721443618"],"aid":5,"autor":"趣处","comNum":3,"content":"http://mp.weixin.qq.com/s?__biz=MzI4MDA0MTUzNA==&mid=212618467&idx=1&sn=05ddcaafdc2a4416def5e83404e7b1c7&scene=1&srcid=0918cK9tB6cQCz4UzbG50tYf&key=dffc561732c22651e2d62342147736b4229382290ab1c6ab3bfc2a852cedfc0cbdb9e8209eac727b4309a0905e11cb39&ascene=1&uin=MjM3OTEwMDI4MA%3D%3D&devicetype=webwx&version=70000001&pass_ticket=x5WbW%2Fd4zR%2F5Klx9sBn4R79FRtc17mXrVQJE94B8omrVky8qEsdMaF9%2FLfeT%2FAPU","favoNum":2,"isf":false,"isp":false,"time":"2015-09-18 00:21:23","title":"聊聊 · 001 | 学生时代，你的最佳约会地是哪里？"}],"resultCount":5,"rowCount":5,"rowEnd":5,"rowStart":0}
     * exception :
     * msg : success
     * result : true
     */

    private DataEntity data;
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

    public boolean getResult() {
        return result;
    }

    public static class DataEntity {
        /**
         * pageCount : 1
         * pageSize : 5
         * pagesNo : 1
         * result : [{"aImg":["http://imgdn.paimeilv.com/1444721523235","http://imgdn.paimeilv.com/1444721394094","http://imgdn.paimeilv.com/1442506776284","http://imgdn.paimeilv.com/1444721443618"],"aid":5,"autor":"趣处","comNum":3,"content":"http://mp.weixin.qq.com/s?__biz=MzI4MDA0MTUzNA==&mid=212618467&idx=1&sn=05ddcaafdc2a4416def5e83404e7b1c7&scene=1&srcid=0918cK9tB6cQCz4UzbG50tYf&key=dffc561732c22651e2d62342147736b4229382290ab1c6ab3bfc2a852cedfc0cbdb9e8209eac727b4309a0905e11cb39&ascene=1&uin=MjM3OTEwMDI4MA%3D%3D&devicetype=webwx&version=70000001&pass_ticket=x5WbW%2Fd4zR%2F5Klx9sBn4R79FRtc17mXrVQJE94B8omrVky8qEsdMaF9%2FLfeT%2FAPU","favoNum":2,"isf":false,"isp":false,"time":"2015-09-18 00:21:23","title":"聊聊 · 001 | 学生时代，你的最佳约会地是哪里？"}]
         * resultCount : 5
         * rowCount : 5
         * rowEnd : 5
         * rowStart : 0
         */

        private int pageCount;
        private int pageSize;
        private int pagesNo;
        private int resultCount;
        private int rowCount;
        private int rowEnd;
        private int rowStart;
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
            /**
             * aImg : ["http://imgdn.paimeilv.com/1444721523235","http://imgdn.paimeilv.com/1444721394094","http://imgdn.paimeilv.com/1442506776284","http://imgdn.paimeilv.com/1444721443618"]
             * aid : 5
             * autor : 趣处
             * comNum : 3
             * content : http://mp.weixin.qq.com/s?__biz=MzI4MDA0MTUzNA==&mid=212618467&idx=1&sn=05ddcaafdc2a4416def5e83404e7b1c7&scene=1&srcid=0918cK9tB6cQCz4UzbG50tYf&key=dffc561732c22651e2d62342147736b4229382290ab1c6ab3bfc2a852cedfc0cbdb9e8209eac727b4309a0905e11cb39&ascene=1&uin=MjM3OTEwMDI4MA%3D%3D&devicetype=webwx&version=70000001&pass_ticket=x5WbW%2Fd4zR%2F5Klx9sBn4R79FRtc17mXrVQJE94B8omrVky8qEsdMaF9%2FLfeT%2FAPU
             * favoNum : 2
             * isf : false
             * isp : false
             * time : 2015-09-18 00:21:23
             * title : 聊聊 · 001 | 学生时代，你的最佳约会地是哪里？
             */

            private int aid;
            private String autor;
            private int comNum;
            private String content;
            private int favoNum;
            private boolean isf;
            private boolean isp;
            private String time;
            private String title;
            private List<String> aImg;

            public void setAid(int aid) {
                this.aid = aid;
            }

            public void setAutor(String autor) {
                this.autor = autor;
            }

            public void setComNum(int comNum) {
                this.comNum = comNum;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setFavoNum(int favoNum) {
                this.favoNum = favoNum;
            }

            public void setIsf(boolean isf) {
                this.isf = isf;
            }

            public void setIsp(boolean isp) {
                this.isp = isp;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setAImg(List<String> aImg) {
                this.aImg = aImg;
            }

            public int getAid() {
                return aid;
            }

            public String getAutor() {
                return autor;
            }

            public int getComNum() {
                return comNum;
            }

            public String getContent() {
                return content;
            }

            public int getFavoNum() {
                return favoNum;
            }

            public boolean getIsf() {
                return isf;
            }

            public boolean getIsp() {
                return isp;
            }

            public String getTime() {
                return time;
            }

            public String getTitle() {
                return title;
            }

            public List<String> getAImg() {
                return aImg;
            }

            @Override
            public String toString() {
                return "PostCardItem{" +
                        "aid=" + aid +
                        ", autor='" + autor + '\'' +
                        ", comNum=" + comNum +
                        ", content='" + content + '\'' +
                        ", favoNum=" + favoNum +
                        ", isf=" + isf +
                        ", isp=" + isp +
                        ", time='" + time + '\'' +
                        ", title='" + title + '\'' +
                        ", aImg=" + aImg +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "ArticleListModel{" +
                "data=" + data +
                ", exception='" + exception + '\'' +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }
}
