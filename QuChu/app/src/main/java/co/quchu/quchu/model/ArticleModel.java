package co.quchu.quchu.model;

/**
 * ArticleModel
 * User: Chenhs
 * Date: 2015-12-08
 * 趣处分类 model
 */
public class ArticleModel {


    private String articleId;
    private String articleCreateUserId;
    private String imageId;
    private String favoriteCount;
    private String readCount;
    private String articleName;
    private String articleComtent;
    private String imageUrl;
    private String userName;
    private String userUrl;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleCreateUserId() {
        return articleCreateUserId;
    }

    public void setArticleCreateUserId(String articleCreateUserId) {
        this.articleCreateUserId = articleCreateUserId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(String favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }
}
