package co.quchu.quchu.base;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.igexin.sdk.PushConsts;

import java.util.Set;

import co.quchu.quchu.R;
import co.quchu.quchu.net.GsonRequest;
import co.quchu.quchu.net.NetApi;
import co.quchu.quchu.utils.LogUtils;
import co.quchu.quchu.view.activity.SearchActivity;

/**
 * Created by no21 on 2016/7/1.
 * email:437943145@qq.com
 * desc :
 */
public class GeTuiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle bundle = intent.getExtras();

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");

                // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
//                boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
//                System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

                if (payload != null) {
                    String data = new String(payload);
                    Log.d("GetuiSdkDemo", "receiver payload : " + data);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("标题" + data)
                            .setContentText("第二行消息")
                            .setTicker("New message");//第一次提示消息的时候显示在通知栏上

                    builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, SearchActivity.class), 0));
                    NotificationManagerCompat notificationManiage = NotificationManagerCompat.from(context);
                    notificationManiage.notify(0, builder.build());
                }
                break;
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                String cid = bundle.getString("clientid");
                LogUtils.e("个推cid" + cid);
                new GsonRequest<String>(NetApi.putGtClientById + "?cId=" + cid, null).start(context);
                break;
        }

        Set<String> keySet = bundle.keySet();
        for (String key : keySet) {
            LogUtils.e("\n推送消息 key:" + key + " values:" + bundle.get(key) + "byteString:");
        }
    }
}
