package co.quchu.quchu.model;

/**
 * Created by linqipeng on 2016/3/4 09:42
 * email:437943145@qq.com
 * desc: 首页tablayout数据实体
 */
public class TagsModel {
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
