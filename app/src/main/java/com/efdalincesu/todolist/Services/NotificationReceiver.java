package com.efdalincesu.todolist.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.efdalincesu.todolist.DBSqlite.DatabaseUtil;
import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;
import com.efdalincesu.todolist.Ui.MainActivity;
import com.efdalincesu.todolist.Utils.Util;

import java.util.ArrayList;

public class NotificationReceiver extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 1;
    DatabaseUtil db;

    @Override
    public void onReceive(Context context, Intent intent) {

        db = new DatabaseUtil(context);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());

        ArrayList<Todo> todos = Util.isToday(db.selectTodosToday());


        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, 0);

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
            builder.setContentText(todos.get(0).getSummary());

        } else if (todos.size() > 1) {

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            String msg = "";
            for (Todo todo : todos)
                msg += todo.getTitle() + "\n";

            bigTextStyle.bigText(msg);
            bigTextStyle.setSummaryText("TODO");
            bigTextStyle.setBigContentTitle("Bugün " + todos.size() + " Adet Todo Var.");
            builder.setContentText("Todo'larınız Var.");
            builder.setStyle(bigTextStyle);
        }

        manager.notify(NOTIFICATION_ID, builder.build());

    }

}
