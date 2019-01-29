package com.efdalincesu.todolist.Services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;
import com.efdalincesu.todolist.Ui.AlarmActivity;
import com.efdalincesu.todolist.Ui.MainActivity;
import com.efdalincesu.todolist.Utils.Util;

import java.util.ArrayList;
import java.util.Calendar;

public class NotifReceiver extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 1;
    AlarmManager alarmManager;
    int alarm;
    private final String ALARM_KEY = "alarm";

    @Override
    public void onReceive(Context context, Intent intent) {

        DatabaseUtil db = new DatabaseUtil(context);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        alarm = Integer.parseInt(preferences.getString(ALARM_KEY, "0"));
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());

        ArrayList<Todo> todos = Util.isToday(db.selectTodosNotNull());


        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent1, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder.setAutoCancel(true);
        builder.setVibrate(new long[]{1000, 500});
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_add);
        builder.setLargeIcon(largeIcon);
        builder.setSound(uri);

        if (todos.size() == 1) {

            builder.setContentTitle(todos.get(0).getTitle());
            builder.setContentText(todos.get(0).getSummary() + " Açıklama");
            builder.addAction(R.drawable.ic_clear, "Sil", pendingIntent);

        } else if (todos.size() > 1) {

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            String msg = "";
            for (Todo todo : todos)
                msg += todo.getTitle() + "\n";

            bigTextStyle.bigText(msg);
            bigTextStyle.setSummaryText("TODO");
            bigTextStyle.setBigContentTitle("Bugün " + todos.size() + " Adet Todo Var.");
            builder.setStyle(bigTextStyle);
        }

        Calendar calendar = Calendar.getInstance();

        if (todos.size() > 0) {

            for (Todo todo : todos) {
                if (todo.getReminder() != null) {

                    Intent intent2 = new Intent(context, AlarmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("todo", todo);
                    intent2.putExtras(bundle);
                    PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent2, 0);
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, todo.getReminderHour());
                    calendar.set(Calendar.MINUTE, (todo.getReminderMinute() + alarm));
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent1);
                }
            }

        }

        if (todos.size() > 0)
            manager.notify(NOTIFICATION_ID, builder.build());

    }
}
