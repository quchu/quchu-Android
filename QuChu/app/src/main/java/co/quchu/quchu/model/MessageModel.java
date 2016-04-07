package co.quchu.quchu.model;

import java.io.Serializable;
import java.util.List;

/**
 * MessageModel
 * User: Chenhs
 * Date: 2016-01-11
 */
public class MessageModel implements Serializable {


    /**
     * pageCount : 1
     * pageSize : 10
     * pagesNo : 1
     * result : [{"come":"no","content":"关注了你","form":"哦咯名学生","formId":69,"formPhoto":"http://7xodsq.com1.z0.glb.clouddn.com/69-app-default-avatar","time":"2016-03-18 19:31:33","type":"follow"}]
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
     * come : no
     * content : 关注了你
     * form : 哦咯名学生
     * formId : 69
     * formPhoto : http://7xodsq.com1.z0.glb.clouddn.com/69-app-default-avatar
     * time : 2016-03-18 19:31:33
     * type : follow
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
        private String come;
        private String content;
        private String form;
        private int formId;
        private String formPhoto;
        private String time;
        private String type;

        public String getCome() {
            return come;
        }

        public void setCome(String come) {
            this.come = come;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public int getFormId() {
            return formId;
        }

        public void setFormId(int formId) {
            this.formId = formId;
        }

        public String getFormPhoto() {
            return formPhoto;
        }

        public void setFormPhoto(String formPhoto) {
            this.formPhoto = formPhoto;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
