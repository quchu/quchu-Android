package co.quchu.quchu.model;

/**
 * Created by Nico on 16/7/5.
 */
public class SceneModel {
    private String height;
    private String rgb;
    private String sceneContent;
    private String sceneCover;
    private int sceneId;
    private String sceneName;
    private String[] sceneTitle;
    private String width;
    private String intro;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
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
}
