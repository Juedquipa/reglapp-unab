package com.example.reglappunab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton btQuiz, btAprende;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAprende = findViewById(R.id.button_aprende);
        btQuiz = findViewById(R.id.button_quiz);

        btAprende.setOnClickListener(view -> {
            Toast.makeText(this, "Boton Aprende", Toast.LENGTH_SHORT).show();
        });
        btQuiz.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoaderActivity.class);
            intent.putExtra("activity", "quiz");
            startActivity(intent);
        });
    }
}