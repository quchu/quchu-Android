package co.quchu.quchu.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.thirdhelp.UserLoginListener;
import co.quchu.quchu.thirdhelp.WechatHelper;
import co.quchu.quchu.utils.LogUtils;


public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private WechatHelper weChatHelper;
    private OnWxShareListenter onWxShareListenter;
//    public static SpreadMapActivity sma;

    @Override
    public void onReq(BaseReq baseReq) {
    }


    public interface OnWxShareListenter {
        public void upMissionSucces();
    }

    public void setOnWxShareListenter(OnWxShareListenter listener) {
        this.onWxShareListenter = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        weChatHelper = new WechatHelper(this);
        weChatHelper.getApi().handleIntent(intent, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        weChatHelper.getApi().handleIntent(intent, this);
    }


    @Override
    public void onResp(BaseResp resp) {
        // TODO Auto-generated method stub
        if (resp instanceof SendMessageToWX.Resp) {
            String result = null;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
//				Handler handler = AppContext.myShareHandler;
//				android.os.Message msg = new android.os.Message();
//				msg.what = AppContext.SHARE_SUCESS;
//				handler.sendMessage(msg);
                    result = "分享成功";
                    Toast.makeText(WXEntryActivity.this, "分享成功", 0).show();
//                    if (null != sma) {
//                        sma.shareEnd();
//                    }
                    LogUtils.json("分享成功");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "分享取消";
                    Toast.makeText(WXEntryActivity.this, "分享取消", 0).show();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "分享被拒绝";
                    Toast.makeText(WXEntryActivity.this, "分享被拒绝", 0).show();
                    break;
                default:
                    result = "分享返回";
                    Toast.makeText(WXEntryActivity.this, "分享返回", 0).show();
                    break;
            }
            finish();
        } else if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp _resp = (SendAuth.Resp) resp;
            weChatHelper.actionFromWX(_resp);
            this.finish();
        }
    }

    //
//    public static void putSpreadMapActivity(SpreadMapActivity spreadMapActivity) {
//        sma = spreadMapActivity;
//    }

}
