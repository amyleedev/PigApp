package com.szmy.pigapp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.szmy.pigapp.R;
import com.szmy.pigapp.activity.HealthAuthActivity;
import com.szmy.pigapp.activity.InfoBuyDetailActivity;
import com.szmy.pigapp.activity.InfoSellDetailActivity;
import com.szmy.pigapp.activity.SlaughterhouseRenZhengActivity;
import com.szmy.pigapp.activity.ZhuChangRenZhengActivity;
import com.szmy.pigapp.agent.AgentRenZhengActivity;
import com.szmy.pigapp.comment.NewsCommentActivity;
import com.szmy.pigapp.daijinquan.DaijinquanListActivity;
import com.szmy.pigapp.entity.NewsEntity;
import com.szmy.pigapp.natural.NaturalAuthActivity;
import com.szmy.pigapp.utils.Helper;
import com.szmy.pigapp.utils.UrlEntry;
import com.szmy.pigapp.vehicle.VehicleCompanyAuthActivity;
import com.umeng.common.message.Log;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class MyPushIntentService extends UmengBaseIntentService {
    private String url = "";
    private int mIndex = 0;
    private static final String TAG = MyPushIntentService.class.getName();

    // private Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
    // R.drawable.ic_launcher);
    // 如果需要打开Activity，请调用Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)；否则无法打开Activity。
//{"type":"xd2u1","id":"9251","userid":"2567","ordertype":"1"}
    @Override
    protected void onMessage(Context context, Intent intent) {
        // 需要调用父类的函数，否则无法统计到消息送达
        super.onMessage(context, intent);
//        System.out.println("----onMessage=");
        try {
            // 可以通过MESSAGE_BODY取得消息体
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            UTrack.getInstance(context).trackMsgClick(msg);
//            System.out.println("----message=" + message);
//            System.out.println("----custom=" + msg.custom);
//            System.out.println("----title=" + msg.title);
//            System.out.println("----text=" + msg.text);
//            System.out.println("----url=" + msg.url);
//            Log.d("----message", message); // 消息体
//            Log.d("----custom=", msg.custom); // 自定义消息的内容
//            Log.d("----title=", msg.title); // 通知标题
//            Log.d("----text=", msg.text); // 通知内容
            url = msg.custom;
            Recommend(msg.title, msg.text, url);
            // code to handle message here
            // ...
            // 完全自定义消息的处理方式，点击或者忽略
            boolean isClickOrDismissed = true;
            if (isClickOrDismissed) {
                // 完全自定义消息的点击统计
                UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
                // 完全自定义消息的忽略统计
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(
                        msg);
            }
            // 使用完全自定义消息来开启应用服务进程的示例代码
            // 首先需要设置完全自定义消息处理方式
            // mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
            // code to handle to start/stop service for app process
            JSONObject json = new JSONObject(msg.custom);
            String topic = json.getString("topic");
            if (topic != null && topic.equals("appName:startService")) {
                // 在友盟portal上新建自定义消息，自定义消息文本如下
                // {"topic":"appName:startService"}
                if (Helper.isServiceRunning(context,
                        NotificationService.class.getName()))
                    return;
                Intent intent1 = new Intent();
                intent1.setClass(context, NotificationService.class);
                context.startService(intent1);
            } else if (topic != null && topic.equals("appName:stopService")) {
                // 在友盟portal上新建自定义消息，自定义消息文本如下
                // {"topic":"appName:stopService"}
                if (!Helper.isServiceRunning(context,
                        NotificationService.class.getName()))
                    return;
                Intent intent1 = new Intent();
                intent1.setClass(context, NotificationService.class);
                context.stopService(intent1);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * �Ƽ�����֪ͨ
     */
    @SuppressLint("NewApi")
    private void Recommend(String title, String content, String costom) throws JSONException {
        JSONObject obj = new JSONObject(costom);
        String type = obj.optString("type");
        Intent intent=new Intent();
        if (type.equals("xd2u1") || type.equals("fb2u2s")) {
            String id=obj.optString("id");
            String userid=obj.optString("userid");
            String orderType=obj.optString("ordertype");
            if (!orderType.equals("2")) {
                intent.setClass(MyPushIntentService.this,InfoSellDetailActivity.class);
              } else {
                intent.setClass(MyPushIntentService.this,InfoBuyDetailActivity.class);
            }
            intent.putExtra("id", id);
            intent.putExtra("userid", userid);
            intent.putExtra("type", orderType);
        }else if (type.equals("cj2zc")){ //交易成功代金券
            String amount = obj.optString("amount");
            String toptip = "";
            intent.setClass(MyPushIntentService.this, DaijinquanListActivity.class);
            intent.putExtra("amount",amount);
            intent.putExtra("toptip",toptip);
        }else if(type.equals("rz2zc")){//认证代金券
            String amount = obj.optString("amount");
            String toptip = "认证成功";
            intent.setClass(MyPushIntentService.this, DaijinquanListActivity.class);
            intent.putExtra("amount",amount);
            intent.putExtra("toptip",toptip);
        }else if(type.equals("rz")){//认证成功
            String userType = obj.optString("userType");
            if (userType.equals("1")) { // 猪场
                intent.setClass(MyPushIntentService.this, ZhuChangRenZhengActivity.class);
            } else if (userType.equals("2")) {// 屠宰场
                intent.setClass(MyPushIntentService.this, SlaughterhouseRenZhengActivity.class);
            } else if (userType.equals("3")) {// 经纪人
                intent.setClass(MyPushIntentService.this, AgentRenZhengActivity.class);
            } else if (userType.equals("4")) {
                intent.setClass(MyPushIntentService.this, VehicleCompanyAuthActivity.class);
            } else if (userType.equals("5")) {
                intent.setClass(MyPushIntentService.this, NaturalAuthActivity.class);
            } else if (userType.equals("6")) {
                intent.setClass(MyPushIntentService.this, HealthAuthActivity.class);
                intent.putExtra("type", "6");
            } else if (userType.equals("7")) {
                intent.setClass(MyPushIntentService.this, HealthAuthActivity.class);
                intent.putExtra("type", "7");
            } else if (userType.equals("8")) {
                intent.setClass(MyPushIntentService.this, HealthAuthActivity.class);
                intent.putExtra("type", "8");
            }
        }else if(type.equals("news2all")){
            intent.setClass(MyPushIntentService.this, NewsCommentActivity.class);
            NewsEntity news = new NewsEntity();
            news.setId(obj.optString("id"));
            Bundle bundle = new Bundle();
            bundle.putSerializable("news", news);
            bundle.putString("url", UrlEntry.ip+"/mNews/newsInfo.html?id="+obj.optString("id"));
            intent.putExtras(bundle);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(
                        BitmapFactory.decodeResource(getResources(),
                                R.drawable.ic_launcher))
                .setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNM.notify(mIndex++, notification);
    }

}