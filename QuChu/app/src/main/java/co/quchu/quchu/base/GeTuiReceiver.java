package co.quchu.quchu.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Set;

import co.quchu.quchu.utils.LogUtils;

/**
 * Created by no21 on 2016/7/1.
 * email:437943145@qq.com
 * desc :
 */
public class GeTuiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        Set<String> keySet = extras.keySet();

        for (String key : keySet) {


            LogUtils.e("\n推送消息 key:" + key + " values:" + extras.get(key) + "byteString:" + extras.getByteArray(key) != null ? new String(extras.getByteArray(key)) : "空");
        }
    }
}
