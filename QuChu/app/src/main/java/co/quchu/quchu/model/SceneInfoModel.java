package co.quchu.quchu.model;

/**
 * Created by Nico on 16/7/11.
 */
public class SceneInfoModel {
    private String height = "";
    private String intro = "";
    private String rgb = "";
    private String sceneContent = "";
    private String sceneCover = "";
    private String sceneName = "";
    private String[] sceneTitle;
    private String width = "";
    private int sceneId;
    private String en;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public void copyFrom(SceneInfoModel sceneInfoModel){
        this.height = sceneInfoModel.getHeight();
        this.intro = sceneInfoModel.getIntro();
        this.intro = sceneInfoModel.getIntro();
        this.rgb = sceneInfoModel.getRgb();
        this.sceneContent = sceneInfoModel.getSceneContent();
        this.sceneCover = sceneInfoModel.getSceneCover();
        this.sceneName = sceneInfoModel.getSceneName();
        this.sceneTitle = sceneInfoModel.getSceneTitle();
        this.width = sceneInfoModel.getWidth();
        this.sceneId = sceneInfoModel.getSceneId();

    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public String getSceneContent() {
        return sceneContent;
    }

    public void setSceneContent(String sceneContent) {
        this.sceneContent = sceneContent;
    }

    public String getSceneCover() {
        return sceneCover;
    }

    public void setSceneCover(String sceneCover) {
        this.sceneCover = sceneCover;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String[] getSceneTitle() {
        return sceneTitle;
    }

    public void setSceneTitle(String[] sceneTitle) {
        this.sceneTitle = sceneTitle;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }
}
