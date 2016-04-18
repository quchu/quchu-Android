package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/4/18.
 */
public class VisitedUsersModel {
    private List<SimpleUserModel> result;
    private int userOutCount;

    public List<SimpleUserModel> getResult() {
        return result;
    }

    public void setResult(List<SimpleUserModel> result) {
        this.result = result;
    }

    public int getUserOutCount() {
        return userOutCount;
    }

    public void setUserOutCount(int userOutCount) {
        this.userOutCount = userOutCount;
    }
}
