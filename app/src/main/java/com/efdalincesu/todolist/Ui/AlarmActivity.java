package com.efdalincesu.todolist.Ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.efdalincesu.todolist.Model.Todo;
import com.efdalincesu.todolist.R;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnTouchListener {

    private final String ALARM_SCHEDULE_KEY = "alarm_schedule";
    Uri uri;
    ImageButton kapat, ertele, alarm;
    TextView title, summary, time;
    Animation animation;
    Ringtone ringtone;
    AlarmManager alarmManager;
    SharedPreferences preferences;
    int alarm_schedule;
    float dY, dX;
    Todo todo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            todo = (Todo) bundle.getSerializable("todo");
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        alarm_schedule = Integer.parseInt(preferences.getString(ALARM_SCHEDULE_KEY, "5"));
        kapat = findViewById(R.id.kapat);
        ertele = findViewById(R.id.ertele);
        alarm = findViewById(R.id.alarm);
        title = findViewById(R.id.title);
        summary = findViewById(R.id.summary);
        time = findViewById(R.id.time);
        alarm.setOnTouchListener(this);


        if (todo != null) {

            animation = AnimationUtils.loadAnimation(this, R.anim.alarm_anim);
            alarm.startAnimation(animation);
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (uri == null) {
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            ringtone = RingtoneManager.getRingtone(this, uri);
            ringtone.play();

            title.setText(todo.getTitle());
            summary.setText(todo.getSummary());
            time.setText(todo.getReminder());

        }else{
            finish();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dY = alarm.getY() - event.getRawY();
                dX = alarm.getX() - event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX() + dX;
                alarm.setX(x);
                if (alarm.getX() < ertele.getX() + 40) {
                    alarm.setX(ertele.getX());
                    if (ringtone.isPlaying()) {
                        ringtone.stop();
                    }
                    alarmSchedule();
                    finish();
                } else if (alarm.getX() > kapat.getX() - 40) {
                    alarm.setX(kapat.getX());
                    if (ringtone.isPlaying()) {
                        ringtone.stop();
                    }
                    finish();
                }
                alarm.clearAnimation();
                break;

            default:
                return false;
        }
        return true;
    }

    private void alarmSchedule() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE) + alarm_schedule));

        Intent intent = new Intent(this, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }
}
