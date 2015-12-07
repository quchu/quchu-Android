package co.quchu.quchu.presenter;

import android.content.Context;

import co.quchu.quchu.net.IRequestListener;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.net.NetService;

/**
 * FeedbackPresenter
 * User: Chenhs
 * Date: 2015-12-07
 */
public class FeedbackPresenter {
    public static void postFeedbackInfo(Context context, String Info, IRequestListener listener) {
        NetService.post(context, String.format(NetApi.FeedBack, Info), null, listener);
    }
}
