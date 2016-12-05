package co.quchu.quchu.baselist.Sample;

/**
 * Created by Nico on 16/11/29.
 */

import co.quchu.quchu.baselist.Base.AppContext;
import co.quchu.quchu.baselist.Base.BasePagerApi;
import co.quchu.quchu.baselist.Base.PagerDataModel;
import java.util.HashMap;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public class WeatherApi extends BasePagerApi {

  private String mPlaceId;

  public WeatherApi(String placeId) {
    this.mPlaceId = placeId;
  }

  @Override public Observable fetch(int pageNo) {
    HashMap<String,String> mParams = new HashMap();
    mParams.put("placeId", mPlaceId);
    return AppContext.getRetrofit().create(PageApi.class).getPageData(mParams,pageNo);
  }

  public interface PageApi{

    @GET("placeReview/getListByPlaceId") Observable<PagerDataModel<CommentsModel>> getPageData(
        @QueryMap HashMap<String, String> placeId, @Query("pageNo") int pageNo);
  }

}