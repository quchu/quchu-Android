package co.quchu.quchu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by no21 on 2016/7/6.
 * email:437943145@qq.com
 * desc :
 */
public class PushMessageBean implements Parcelable {

    /**
     * title : 标题
     * content : 内容
     * type : 01
     * eventId : 1
     * eventRemark : 事件备注
     * eventCreateTime : 2014-12-12
     */


    //eventId:根据类别，打开应用相应页面的ID type: 01 为趣处ID 02:场景ID 03：文章ID

    private String title;
    private String content;
    private String type;
    private String eventId;
    private String eventRemark;
    private long eventTimeStamp;
    private String eventCreateTime;

    public long getEventTimeStamp() {
        return eventTimeStamp;
    }

    public void setEventTimeStamp(long eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventRemark() {
        return eventRemark;
    }

    public void setEventRemark(String eventRemark) {
        this.eventRemark = eventRemark;
    }

    public String getEventCreateTime() {
        return eventCreateTime;
    }

    public void setEventCreateTime(String eventCreateTime) {
        this.eventCreateTime = eventCreateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.type);
        dest.writeString(this.eventId);
        dest.writeString(this.eventRemark);
        dest.writeString(this.eventCreateTime);
        dest.writeLong(this.eventTimeStamp);
    }

    public PushMessageBean() {
    }

    protected PushMessageBean(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.type = in.readString();
        this.eventId = in.readString();
        this.eventRemark = in.readString();
        this.eventCreateTime = in.readString();
        this.eventTimeStamp = in.readLong();
    }

    public static final Creator<PushMessageBean> CREATOR = new Creator<PushMessageBean>() {
        @Override
        public PushMessageBean createFromParcel(Parcel source) {
            return new PushMessageBean(source);
        }

        @Override
        public PushMessageBean[] newArray(int size) {
            return new PushMessageBean[size];
        }
    };

    @Override public String toString() {
        return "PushMessageBean{" +
            "title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", type='" + type + '\'' +
            ", eventId='" + eventId + '\'' +
            ", eventRemark='" + eventRemark + '\'' +
            ", eventTimeStamp=" + eventTimeStamp +
            ", eventCreateTime='" + eventCreateTime + '\'' +
            '}';
    }
}
