package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nikolai on 2016/4/17.
 */
public class SimpleQuchuDetailAnlysisModel  {

    private int userOutCount;
    private List<TagsModel> result;

    public int getUserOutCount() {
        return userOutCount;
    }

    public void setUserOutCount(int userOutCount) {
        this.userOutCount = userOutCount;
    }

    public List<TagsModel> getResult() {
        return result;
    }

    public void setResult(List<TagsModel> result) {
        this.result = result;
    }
}
