package com.e2e4gu.test.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.e2e4gu.test.R;

public class SplashActivity extends AppCompatActivity {
    private final int TIMEOUT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Имитация загрузки приложения
        new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT * 1000);
                startActivity(new Intent(this, MapActivity.class));
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }
}