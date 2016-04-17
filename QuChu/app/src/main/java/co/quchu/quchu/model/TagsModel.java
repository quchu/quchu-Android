package co.quchu.quchu.model;

/**
 * Created by linqipeng on 2016/3/4 09:42
 * email:437943145@qq.com
 * desc: 首页tablayout数据实体
 */
public class TagsModel {
    private String zh;
    private String en;
    private int tagId;
    private String code;
    private boolean isSend;
    private int count;

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagsModel tagsModel = (TagsModel) o;

        return tagId == tagsModel.tagId;

    }

    @Override
    public int hashCode() {
        return tagId;
    }
}
