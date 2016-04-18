package co.quchu.quchu.model;

/**
 * Created by no21 on 2016/4/18.
 * email:437943145@qq.com
 * desc :
 */
public class BindEntity {

    /**
     * result : false
     * msg : 已被绑定
     */

    private boolean result;
    private String msg;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
