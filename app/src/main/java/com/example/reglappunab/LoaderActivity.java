package com.example.reglappunab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class LoaderActivity extends AppCompatActivity {
    String activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        activity = getIntent().getStringExtra("activity");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Objects.equals(activity, "quiz")){
                    startActivity(new Intent(LoaderActivity.this, QuizActivity.class));
                }
                else {
                    startActivity(new Intent(LoaderActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 1000);
    }
}