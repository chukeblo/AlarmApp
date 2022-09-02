package com.example.alarmapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarmapp.databinding.ActivityMainBinding;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String TITLE = "Alarm App";

    private ActivityMainBinding binding;
    private boolean isAlarmEnabled = false;
    private AlarmManager alarmManager;
    private PendingIntent pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, TITLE, NotificationManager.IMPORTANCE_DEFAULT
            );

            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }


        // 開始ボタン
        binding.startButton.setOnClickListener(view -> {
            // 時間をセットする
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            int alarmMins = Integer.parseInt(binding.alarmMinEditText.getText().toString());
            calendar.add(Calendar.MINUTE, alarmMins);
            int alarmSecs = Integer.parseInt(binding.alarmSecEditText.getText().toString());
            calendar.add(Calendar.SECOND, alarmSecs);

            Intent intent = new Intent(getApplicationContext(), AlarmNotifier.class);
            intent.putExtra("ALARM_SEC_VALUE", alarmSecs);
            intent.putExtra("ALARM_MIN_VALUE", alarmMins);
            intent.putExtra("Request Code", REQUEST_CODE);
            pending = PendingIntent.getBroadcast(
                    getApplicationContext(), REQUEST_CODE, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending);
                isAlarmEnabled = true;

                Toast.makeText(getApplicationContext(),
                        "alarm has been set",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        binding.cancelButton.setOnClickListener(view -> {
            if (!isAlarmEnabled) {
                Toast.makeText(getApplicationContext(),
                        "no alarm has been set",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Intent intent = new Intent(getApplicationContext(), AlarmNotifier.class);
            if (alarmManager == null) {
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            }
            cancelAlarm();
            Toast.makeText(getApplicationContext(),
                    "alarm has been canceled",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alarmManager.cancel(pending);
    }

    private void cancelAlarm() {
        if (isAlarmEnabled && alarmManager != null && pending != null) {
            alarmManager.cancel(pending);
            pending = null;
            isAlarmEnabled = false;
        }
    }
}