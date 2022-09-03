package com.example.alarmapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmExecutionReceiver extends BroadcastReceiver {
    public static final String ALARM_EXECUTED = "com.example.alarmapp.ALARM_EXECUTED";

    private final AlarmExecutionListener listener;

    AlarmExecutionReceiver(AlarmExecutionListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        listener.onAlarmExecuted();
    }

    public interface AlarmExecutionListener {
        void onAlarmExecuted();
    }
}
