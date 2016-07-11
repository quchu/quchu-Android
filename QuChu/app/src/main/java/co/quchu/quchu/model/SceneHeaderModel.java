package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/7/11.
 */
public class SceneHeaderModel {

    private List<DetailModel> placeInfo;
    private String title;

    public List<DetailModel> getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(List<DetailModel> placeInfo) {
        this.placeInfo = placeInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
