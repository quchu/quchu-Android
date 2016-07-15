package co.quchu.quchu.model;

/**
 * Created by Nico on 16/7/8.
 */
public class SimpleArticleModel {

    private int articleId;
    private int imageId;
    private String articleName;
    private String articleComtent;
    private String imageUrl;
    private boolean isFavorite;


    public void copyFrom(SimpleArticleModel simpleArticleModel){
        this.setArticleName(simpleArticleModel.getArticleName());
        this.setArticleId(simpleArticleModel.getArticleId());
        this.setImageId(simpleArticleModel.getImageId());
        this.setArticleComtent(simpleArticleModel.getArticleComtent());
        this.setImageUrl(simpleArticleModel.getImageUrl());
        this.setFavorite(simpleArticleModel.isFavorite);
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleComtent() {
        return articleComtent;
    }

    public void setArticleComtent(String articleComtent) {
        this.articleComtent = articleComtent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
