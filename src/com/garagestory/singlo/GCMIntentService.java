package com.garagestory.singlo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.garagestory.singlo.users.Main;
import com.google.android.gcm.GCMBaseIntentService;

import java.util.List;

public class GCMIntentService extends GCMBaseIntentService {
    private static final String tag = "GCMIntentService";
    private static final String PROJECT_ID = "884305720962";
    private static String REG_ID = "";
    private NotificationManager nm = null;
    boolean isForeground = false;

    public GCMIntentService() {
        this(PROJECT_ID);
    }

    public GCMIntentService(String project_id) {
        super(project_id);
    }

    public static String getProjectId() {
        return PROJECT_ID;
    }

    public static void setRegId(String reg_id) {
        REG_ID = reg_id;
    }

    public static String getRegId() {
        return REG_ID;
    }

    /**
     * 푸시로 받은 메시지
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        String message = "";

        if (nm == null) {
            nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        for (String key : b.keySet()) {
            String value = b.get(key).toString();
            //Log.d(tag, "onMessage. " + key + " : " + value);
            //message 에서 꺼내서 text 보내주기
            if (key.equals("message")) {
                message = value;
            }
        }
        isForeground = isForeground(context);
        setNotification(message);
    }

    /**
     * 에러 발생시
     */
    @Override
    protected void onError(Context context, String errorId) {
        Log.d(tag, "onError. errorId : " + errorId);
    }

    /**
     * 단말에서 GCM 서비스 등록 했을 때 등록 id를 받는다
     */
    @Override
    protected void onRegistered(Context context, String regId) {
        Log.d(tag, "onRegistered. regId : " + regId);
        SharedPreferences prefs = getSharedPreferences("Singlo", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("pushtoken", regId);
        ed.commit();
        setRegId(regId);
    }

    /**
     * 단말에서 GCM 서비스 등록 해지를 하면 해지된 등록 id를 받는다
     */
    @Override
    protected void onUnregistered(Context context, String regId) {
        Log.d(tag, "onUnregistered. regId : " + regId);
        SharedPreferences prefs = getSharedPreferences("Singlo", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("pushtoken", "");
        ed.commit();
    }

    private void setNotification(String msg) {
        //set Intent
        Intent intent = new Intent(getApplicationContext(), Main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("싱글로")
                .setContentText(msg)
                .setAutoCancel(true) // 알림바에서 자동 삭제
                .setVibrate(new long[]{1000, 2000, 1000, 3000, 1000, 4000});


        builder.setContentIntent(pendingIntent);


        nm.notify(0213, builder.build());
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public boolean isForeground(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (ctx.getPackageName().equals(appProcess.processName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
