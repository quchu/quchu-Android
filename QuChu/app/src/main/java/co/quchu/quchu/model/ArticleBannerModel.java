package co.quchu.quchu.model;

/**
 * Created by Nico on 16/7/7.
 */
public class ArticleBannerModel {

    public String articleName;
    public String articleComtent;
    public String imageUrl;
    public int articleCreateUserId;
    public int imageId;
    public int readCount;
    public int articleId;

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

    public int getArticleCreateUserId() {
        return articleCreateUserId;
    }

    public void setArticleCreateUserId(int articleCreateUserId) {
        this.articleCreateUserId = articleCreateUserId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}
