package co.quchu.quchu.model;

/**
 * Created by admin on 2016/3/10.
 */
public class QuchuEventModel {

    private int mFlag;
    private Object mContent;

    public QuchuEventModel(int flag, Object ojbContent) {
        this.mFlag = flag;
        this.mContent = ojbContent;
    }

    public int getFlag() {
        return mFlag;
    }

    public void setFlag(int flag) {
        this.mFlag = flag;
    }

    public Object getContent() {
        return mContent;
    }

    public void setContent(Object content) {
        this.mContent = content;
    }
}
