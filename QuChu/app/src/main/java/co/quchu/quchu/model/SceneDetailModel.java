package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/7/11.
 */
public class SceneDetailModel {

    private SceneInfoModel sceneInfo;
    private PagerModel<DetailModel> placeList;
    private List<SceneHeaderModel> bestList;
    private SimpleArticleModel article;
    private int[]placeIds;

    public SceneInfoModel getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfoModel sceneInfo) {
        this.sceneInfo = sceneInfo;
    }

    public PagerModel<DetailModel> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(PagerModel<DetailModel> placeList) {
        this.placeList = placeList;
    }

    public int[] getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(int[] placeIds) {
        this.placeIds = placeIds;
    }

    public List<SceneHeaderModel> getBestList() {
        return bestList;
    }

    public void setBestList(List<SceneHeaderModel> bestList) {
        this.bestList = bestList;
    }

    public SimpleArticleModel getArticleModel() {
        return article;
    }

    public void setArticleModel(SimpleArticleModel articleModel) {
        this.article = articleModel;
    }
}
