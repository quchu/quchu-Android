package co.quchu.quchu.model;

import java.util.List;

/**
 * RecommendTagsModel
 * User: Chenhs
 * Date: 2016-03-03
 */
public class RecommendTagsModel {


    /**
     * data : [{"zh":"住宿","en":"lodging","isSend":true}]
     * exception : 二级分类列表
     * msg : success
     * result : true
     */

    private String exception;
    private String msg;
    private boolean result;
    /**
     * zh : 住宿
     * en : lodging
     * isSend : true
     */

    private List<TagsModel> data;

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setData(List<TagsModel> data) {
        this.data = data;
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

    public List<TagsModel> getData() {
        return data;
    }

    public static class TagsModel {
        private String zh;
        private String en;
        private boolean isSend;

        public void setZh(String zh) {
            this.zh = zh;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public void setIsSend(boolean isSend) {
            this.isSend = isSend;
        }

        public String getZh() {
            return zh;
        }

        public String getEn() {
            return en;
        }

        public boolean isIsSend() {
            return isSend;
        }
    }
}
