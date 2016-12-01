package co.quchu.quchu.refactor;

import co.quchu.quchu.BuildConfig;

/**
 * Created by mwb on 16/9/24.
 */
public class QuChuApiManage {

  public static final String API_HOST;

  /**
   * 正式环境
   */
  private static String HOST_RELEASE = "http://www.quchu.co/app-main-service/";
  /**
   * 集成测试环境
   */
  private static String HOST_UAT = "http://uat.quchu.co/app-main-service/";
  /**
   * 开发环境
   */
  private static String HOST_SIT = "http://sit.quchu.co/app-main-service/";

  static {
    API_HOST = BuildConfig.API_SERVER == 0 ? HOST_RELEASE
        : BuildConfig.API_SERVER == 1 ? HOST_UAT : HOST_SIT;
  }

  //用户登录
  static final String LOGIN_URI = "login/android";
}
