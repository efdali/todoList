package com.efdalincesu.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                }catch (InterruptedException ex){

                }finally {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });

        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
