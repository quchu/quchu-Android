package co.quchu.quchu.thirdhelp;

/**
 * UserLoginListener
 * User: Chenhs
 * Date: 2015-11-30
 * 登录回调
 */
public interface UserLoginListener {
    /**
     * @param type  1手机,2微信 3微博
     */
    void loginSuccess(int type, String token, String appId);
}
