package co.quchu.quchu.refactor;

import com.sina.weibo.sdk.utils.MD5;

import java.util.HashMap;
import java.util.Map;

import co.quchu.quchu.base.AppContext;
import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.refactor.mvp.BasePresenter;
import co.quchu.quchu.refactor.rxjava.BaseSubscriber;
import co.quchu.quchu.utils.SPUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by mwb on 2016/11/30.
 */
public class LoginPresenterImpl extends BasePresenter<LoginContract.LoginView> implements LoginContract.LoginPresenter {

  public LoginPresenterImpl(LoginContract.LoginView view) {
    attachView(view);
  }

  public void login(String userName, String password) {
    mMvpView.showLoading("正在登录", false);
    Map<String, String> map = new HashMap<>();
    map.put("j_username", userName);
    map.put("j_password", MD5.hexdigest(password));
    map.put("equip", StringUtils.getMyUUID());
    execute(mService.login(map), new BaseSubscriber<UserInfoModel>() {
      @Override
      public void onSuccess(UserInfoModel data) {
        SPUtils.setUserToken(AppContext.mContext, data.getToken());
        AppContext.token = data.getToken();
        AppContext.user = data;
        mMvpView.onSuccess(data);
      }

      @Override
      public void onFailure(String msg, String exception) {
        mMvpView.onFailure(msg, exception);
      }

      @Override
      public void onFinish() {
        mMvpView.hideLoading();
      }
    });
  }

  public void visitorRegister() {
    mMvpView.showLoading("正在退出登录", false);
    Map<String, String> map = new HashMap<>();
    map.put("visitors", "1");
    map.put("equip", StringUtils.getMyUUID());
    execute(mService.visitorRegister(map), new BaseSubscriber<UserInfoModel>() {
      @Override
      public void onSuccess(UserInfoModel data) {
//        SPUtils.setUserToken(AppContext.mContext, data.getToken());
//        AppContext.token = data.getToken();
//        AppContext.user = data;
        mMvpView.onSuccess(data);
      }

      @Override
      public void onFailure(String msg, String exception) {
        mMvpView.onFailure(msg, exception);
      }

      @Override
      public void onFinish() {
        mMvpView.hideLoading();
      }
    });
  }
}
