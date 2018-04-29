package com.liuxin.loadview;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private LoadView loadView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadView = findViewById(R.id.loadview);

    }

    public void downLoad(View view) {

   new TimeDown(12*1000,1000).start();

    }

    private class TimeDown extends CountDownTimer{

        private float i=0;
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            i=0;
            loadView.setPercent(0);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("lx","onTick"+millisUntilFinished);
            loadView.setPercent(i);
            i=i+0.083333f;
        }

        @Override
        public void onFinish() {
           loadView.setPercent(1f);
        }
    }




}
