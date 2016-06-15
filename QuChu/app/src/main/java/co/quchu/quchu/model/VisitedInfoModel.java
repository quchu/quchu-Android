package co.quchu.quchu.model;

import java.util.ArrayList;

/**
 * Created by Nico on 16/4/19.
 */
public class VisitedInfoModel {

    private int userCount;
    private int score;
    private ArrayList<TagsModel> result;

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<TagsModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<TagsModel> result) {
        this.result = result;
    }
}
