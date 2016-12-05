package co.quchu.quchu.refactor;

import co.quchu.quchu.model.UserInfoModel;
import co.quchu.quchu.refactor.mvp.BaseView;

/**
 * Created by mwb on 2016/12/5.
 */
public class LoginContract {

  public interface LoginView extends BaseView {

    void onSuccess(UserInfoModel data);
  }

  public interface LoginPresenter {

    void login(String userName, String password);

    void visitorRegister();
  }
}
