package co.quchu.quchu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by no21 on 2016/6/29.
 * email:437943145@qq.com
 * desc :
 */
public class SearchCategoryBean implements Parcelable {
    /**
     * tagId : 2000
     * zh : 美食
     * en : hungry
     * code :
     * iconUrl : http://7xo7f0.com1.z0.glb.clouddn.com/icon/ic_ele@2x.png
     */

    private int tagId;
    private String zh;
    private String en;
    private String code;
    private String iconUrl;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tagId);
        dest.writeString(this.zh);
        dest.writeString(this.en);
        dest.writeString(this.code);
        dest.writeString(this.iconUrl);
    }

    public SearchCategoryBean() {
    }

    protected SearchCategoryBean(Parcel in) {
        this.tagId = in.readInt();
        this.zh = in.readString();
        this.en = in.readString();
        this.code = in.readString();
        this.iconUrl = in.readString();
    }

    public static final Parcelable.Creator<SearchCategoryBean> CREATOR = new Parcelable.Creator<SearchCategoryBean>() {
        @Override
        public SearchCategoryBean createFromParcel(Parcel source) {
            return new SearchCategoryBean(source);
        }

        @Override
        public SearchCategoryBean[] newArray(int size) {
            return new SearchCategoryBean[size];
        }
    };
}
