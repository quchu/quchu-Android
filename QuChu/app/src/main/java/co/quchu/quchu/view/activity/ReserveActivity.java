package co.quchu.quchu.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.utils.StringUtils;

/**
 * ReserveActivity
 * User: Chenhs
 * Date: 2016-01-06
 */
public class ReserveActivity extends BaseActivity {
    String placeUrl = "";
    @Bind(R.id.reserve_wv)
    WebView reserveWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        ButterKnife.bind(this);
        initTitleBar();
        placeUrl = getIntent().getStringExtra("PlaceUrl");
        WebSettings settings = reserveWv.getSettings();
        //设置 缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
// 开启 DOM storage API 功能
        settings.setDomStorageEnabled(false);
        settings.setJavaScriptEnabled(true);
        if (StringUtils.isEmpty(placeUrl)) {
            Toast.makeText(this, "(= _ =)! 迷路了!", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            //    placeUrl="http://www.baidu.com";
            reserveWv.loadUrl(placeUrl);
            reserveWv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageCommitVisible(WebView view, String url) {
                    LogUtils.json("onPageCommitVisible url=" + url);
                    super.onPageCommitVisible(view, url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    LogUtils.json("onPageStarted url=" + url);
                    if (url.startsWith("http://"))
                        super.onPageStarted(view, url, favicon);
                    else
                        reserveWv.loadUrl(placeUrl);
                }

            });

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (reserveWv.canGoBack() && !android.os.Build.BRAND.equals("Meizu")) {
                reserveWv.goBack();//返回上一页面
                return true;
            } else {
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
