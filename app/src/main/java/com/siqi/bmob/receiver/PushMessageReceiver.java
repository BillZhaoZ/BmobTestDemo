package com.siqi.bmob.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.siqi.bmob.Notify;
import com.siqi.bmob.R;

import cn.bmob.push.PushConstants;

/**
 * 推送接收器  数据处理
 * Created by Bill on 17/8/24 14:20
 */
public class PushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = intent.getStringExtra("msg");

        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

            // 吐司和Log
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Logger.i("客户端收到推送消息：" + msg);

            // 任务栏通知
            Notify notify = new Notify(context);
            notify.setId(msg.hashCode());
            notify.setTitle(msg);
            notify.setAutoCancel(true);
            notify.setSmallIcon(R.mipmap.ic_launcher);
            notify.notification();

            notify.show();
        }
    }
}
