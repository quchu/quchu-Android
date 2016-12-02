package co.quchu.quchu.refactor;

import java.util.Map;

import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.refactor.entity.BaseResponse;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by mwb on 2016/11/29.
 */
public interface QuChuApiService {

  /**
   * 登录
   */
  @POST("login/android")
  Observable<BaseResponse<UserInfoModel>> login(@QueryMap Map<String, String> map);

  /**
   * 游客登录
   */
  @POST("mregister")
  Observable<BaseResponse<UserInfoModel>> visitorRegister(@QueryMap Map<String, String> map);
}
