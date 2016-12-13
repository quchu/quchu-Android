package co.quchu.quchu.base;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import com.google.gson.Gson;
import com.igexin.sdk.PushConsts;

import java.util.Random;
import java.util.Set;

import co.quchu.quchu.R;
import co.quchu.quchu.model.PushMessageBean;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.RecommendActivity;

/**
 * Created by no21 on 2016/7/1.
 * email:437943145@qq.com
 * desc :
 */
public class GeTuiReceiver extends BroadcastReceiver {
    public static final String REQUEST_KEY_MODEL = "model";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");

//                String taskid = bundle.getString("taskid");
//                String messageid = bundle.getString("messageid");
                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
//                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    Log.d("GetuiSdkDemo", "receiver payload : " + data);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    Gson gson = new Gson();
                    PushMessageBean messageBean = gson.fromJson(data, PushMessageBean.class);

                    builder.setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(messageBean.getTitle())
                            .setContentText(messageBean.getContent())
                            .setAutoCancel(true)
                            .setTicker(messageBean.getContent());//第一次提示消息的时候显示在通知栏上

                    Intent pushIntent = new Intent(context, RecommendActivity.class);
                    pushIntent.putExtra(RecommendActivity.BUNDLE_KEY_FROM_PUSH,true);
                    pushIntent.putExtra(REQUEST_KEY_MODEL, messageBean);

                    int id = 10086;
                    builder.setContentIntent(PendingIntent.getActivity(context, id, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                    NotificationManagerCompat notificationManiage = NotificationManagerCompat.from(context);
                    notificationManiage.notify(id, builder.build());
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)

                if (!TextUtils.isEmpty(AppContext.token)) {
                    String cid = bundle.getString("clientid");
                    LogUtils.e("个推cid" + cid);
                    new GsonRequest<String>(NetApi.putGtClientById + "?cId=" + cid, null).start(context);
                }
                break;
        }
        Set<String> keySet = bundle.keySet();
        for (String key : keySet) {
            LogUtils.e("\n推送消息 key:" + key + " values:" + bundle.get(key) + "byteString:");
        }
    }


}
