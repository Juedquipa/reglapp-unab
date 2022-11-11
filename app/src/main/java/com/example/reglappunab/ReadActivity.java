package com.example.reglappunab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        TextView title = findViewById(R.id.title_textview);
        TextView content = findViewById(R.id.content_textview);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SeccionEnsenanza valor = (SeccionEnsenanza) extras.get("key");


            title.setText(valor.getNombre().replace("\\n", "\n\n"));
            content.setText(valor.getContent().replace("\\n", "\n\n"));
            content.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}