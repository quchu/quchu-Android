package co.quchu.quchu.model;

/**
 * GetCaptchModel
 * User: Chenhs
 * Date: 2015-10-20
 */
public class GetCaptchModel {

    /**
     * data : {"result":"000000","code":"875482"}
     * exception : null
     * msg : success
     * result : true
     */

    private DataEntity data;
    private String exception="";
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

    public Object getException() {
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
         * result : 000000
         * code : 875482
         */

        private String result;
        private String code;

        public void setResult(String result) {
            this.result = result;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getResult() {
            return result;
        }

        public String getCode() {
            return code;
        }
    }
}
