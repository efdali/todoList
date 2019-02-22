package com.efdalincesu.todolist.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.efdalincesu.todolist.R;

public class SplashScreen extends AppCompatActivity {

    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);

        SharedPreferences preferences = getSharedPreferences("ICON", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (!preferences.getBoolean("IS_ICON_CREATED", false)) {
            createShortcut();
            editor.putBoolean("IS_ICON_CREATED", true).commit();
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException ex){

                }finally {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }
        });

        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void createShortcut() {
        Intent intentShortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        Parcelable appicon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher_background);
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, appicon);
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), MainActivity.class));
        intentShortcut.putExtra("duplicate", false);
        sendBroadcast(intentShortcut);
    }
}
