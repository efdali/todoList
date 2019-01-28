package com.efdalincesu.todolist.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent intent1=new Intent(context, AlarmManagerService.class);
            context.startService(intent1);
    }
}
