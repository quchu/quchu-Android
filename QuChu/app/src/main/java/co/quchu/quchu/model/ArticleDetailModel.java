package co.quchu.quchu.model;

/**
 * Created by Nico on 16/7/8.
 */
public class ArticleDetailModel {

    private SimpleArticleModel article;
    private PagerModel<SimplePlaceModel> placeList;


    public SimpleArticleModel getArticle() {
        return article;
    }

    public void setArticle(SimpleArticleModel article) {
        this.article = article;
    }

    public PagerModel<SimplePlaceModel> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(PagerModel<SimplePlaceModel> placeList) {
        this.placeList = placeList;
    }
}
