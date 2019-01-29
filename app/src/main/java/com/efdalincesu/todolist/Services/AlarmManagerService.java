package com.efdalincesu.todolist.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class AlarmManagerService extends Service {

    AlarmManager alarmManager;

    @Override
    public void onCreate() {
        super.onCreate();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);

        Intent intent = new Intent(getBaseContext(), NotifReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
