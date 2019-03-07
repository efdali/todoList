package com.efdalincesu.todolist.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.Ui.AlarmActivity;
import com.efdalincesu.todolist.Utils.Util;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private final String ALARM_KEY = "alarm";
    AlarmManager alarmManager;
    int alarm;

    @Override
    public void onReceive(Context context, Intent intent) {

        DatabaseUtil db = new DatabaseUtil(context);

        ArrayList<Todo> todos = Util.isToday(db.selectTodosToday());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        alarm = Integer.parseInt(preferences.getString(ALARM_KEY, "0"));
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (todos.size() > 0) {

            for (Todo todo : todos) {
                if (todo.getReminder() != null) {
                    Calendar calendar = Calendar.getInstance();
                    Intent intent2 = new Intent(context, AlarmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("todo", todo);
                    intent2.putExtras(bundle);
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent2, 0);
                    alarmManager.cancel(pendingIntent1);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, todo.getReminderHour());
                    calendar.set(Calendar.MINUTE, todo.getReminderMinute());
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent1);
                }
            }

        }

    }
}
