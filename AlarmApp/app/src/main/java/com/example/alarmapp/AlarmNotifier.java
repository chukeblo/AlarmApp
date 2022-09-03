package com.example.alarmapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AlarmNotifier extends BroadcastReceiver {
    public static final String ALARM_MIN_VALUE = "ALARM_MIN_VALUE";
    public static final String ALARM_SEC_VALUE = "ALARM_SEC_VALUE";

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String channelId = "CHANNEL_ID";
        String title = context.getString(R.string.app_name);

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.JAPAN);
        String cTime = simpleDateFormat.format(currentTime);
        int alarmMinValue = intent.getIntExtra(ALARM_MIN_VALUE, 0);
        int alarmSecValue = intent.getIntExtra(ALARM_SEC_VALUE, 0);
        String message = "時間になりました。 " + cTime + " 設定時間: " + alarmMinValue + "分" + alarmSecValue + "秒";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.btn_star)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(R.string.app_name, builder.build());

        Intent executionIntent = new Intent(AlarmExecutionReceiver.ALARM_EXECUTED);
        context.sendBroadcast(executionIntent);
    }
}
