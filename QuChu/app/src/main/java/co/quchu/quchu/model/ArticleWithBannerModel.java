package co.quchu.quchu.model;

import java.util.List;

/**
 * Created by Nico on 16/7/7.
 */
public class ArticleWithBannerModel {
    private List<ArticleBannerModel> articleTitleList;
    private PagerModel<ArticleModel> articleList;

    public List<ArticleBannerModel> getArticleTitleList() {
        return articleTitleList;
    }

    public void setArticleTitleList(List<ArticleBannerModel> articleTitleList) {
        this.articleTitleList = articleTitleList;
    }


    public PagerModel<ArticleModel> getArticleList() {
        return articleList;
    }

    public void setArticleList(PagerModel<ArticleModel> articleList) {
        this.articleList = articleList;
    }
}
