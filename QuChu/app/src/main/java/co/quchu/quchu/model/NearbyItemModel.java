package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/4/11.
 */
public class NearbyItemModel {
    private int placeId;
    private String cover;
    private String name;
    private List<TagsModel> tags;

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TagsModel> getTags() {
        return tags;
    }

    public void setTags(List<TagsModel> tags) {
        this.tags = tags;
    }
}
