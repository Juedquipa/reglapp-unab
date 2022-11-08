package com.example.reglappunab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {
    Button btRespuesta1, btRespuesta2, btRespuesta3, btRespuesta4;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    TextView tvPregunta, tvMarcador;
    int totalQuestions, correctQuestions;
    ArrayList<Pregunta> preguntas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        btRespuesta1 = findViewById(R.id.button_respuesta_1);
        btRespuesta2 = findViewById(R.id.button_respuesta_2);
        btRespuesta3 = findViewById(R.id.button_respuesta_3);
        btRespuesta4 = findViewById(R.id.button_respuesta_4);
        tvPregunta = findViewById(R.id.tv_pregunta);
        tvMarcador = findViewById(R.id.tv_marcador);
        totalQuestions = -1;
        correctQuestions = 0;

        preguntas = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("preguntas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Pregunta p;
                        Respuesta r0, r1, r2, r3;
                        HashMap data;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.e("TAG", String.valueOf(document.getData().size()));
                            for (int i = 0; i < document.getData().size(); i++) {
                                data = (HashMap) document.get(String.valueOf(i));
                                p = new Pregunta((String) data.get("pregunta"));
                                r0 = new Respuesta((String) data.get("0"), (String) data.get("j0"));
                                r1 = new Respuesta((String) data.get("1"), (String) data.get("j1"));
                                r2 = new Respuesta((String) data.get("2"), (String) data.get("j2"));
                                r3 = new Respuesta((String) data.get("3"), (String) data.get("j3"));
                                ArrayList<Respuesta> respuestas = new ArrayList<>(Arrays.asList(r0, r1, r2, r3));
                                Log.e("TAG",p.getPregunta());
                                int correct = ((Long) data.get("correcta")).intValue();
                                respuestas.get(correct).setCorrecta(true);
                                p.setRespuestas(respuestas);
                                preguntas.add(p);
                            }
                        }
                        Collections.shuffle(preguntas);
                        nextQuestion();
                    } else {
                        Log.e("TAG", "Error getting documents.", task.getException());
                    }
                });
    }

    public void showPopup(String tittle, String message){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(tittle);
        dialogBuilder.setMessage(message);
        dialog = dialogBuilder.create();
        dialog.setButton(Dialog.BUTTON_POSITIVE, "Entendido", (dialogInterface, i) -> {
            if (Objects.equals(tittle, "¡Correcto!")){
                correctQuestions+=1;
                tvMarcador.setText(correctQuestions + "/10");
            }
            nextQuestion();
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void nextQuestion(){
        totalQuestions += 1;
        if (totalQuestions<=preguntas.size()){
            Pregunta pregunta = preguntas.get(totalQuestions);
            ArrayList<Respuesta> respuestas = pregunta.getRespuestas();

            tvPregunta.setText(pregunta.getPregunta());
            btRespuesta1.setText(respuestas.get(0).getRespuesta());
            btRespuesta2.setText(respuestas.get(1).getRespuesta());
            btRespuesta3.setText(respuestas.get(2).getRespuesta());
            btRespuesta4.setText(respuestas.get(3).getRespuesta());

            btRespuesta1.setOnClickListener(v -> {
                String message = "Incorrecto :(";
                if (respuestas.get(0).isCorrecta()){
                    message = "¡Correcto!";
                }
                showPopup(message, respuestas.get(0).getJustificacion());
            });

            btRespuesta2.setOnClickListener(v -> {
                String message = "Incorrecto :(";
                if (respuestas.get(1).isCorrecta()){
                    message = "¡Correcto!";
                }
                showPopup(message, respuestas.get(1).getJustificacion());
            });

            btRespuesta3.setOnClickListener(v -> {
                String message = "Incorrecto :(";
                if (respuestas.get(2).isCorrecta()){
                    message = "¡Correcto!";
                }
                showPopup(message, respuestas.get(2).getJustificacion());
            });

            btRespuesta4.setOnClickListener(v -> {
                String message = "Incorrecto :(";
                if (respuestas.get(3).isCorrecta()){
                    message = "¡Correcto!";
                }
                showPopup(message, respuestas.get(3).getJustificacion());
            });
        }
        else {
            endGame();
        }
    }

    public void endGame(){

    }
}