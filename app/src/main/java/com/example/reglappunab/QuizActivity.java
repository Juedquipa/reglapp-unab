package com.example.reglappunab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    Button btRespuesta1, btRespuesta2, btRespuesta3, btRespuesta4;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    TextView tvPregunta, tvMarcador, tvTimer;
    int totalQuestions, correctQuestions;
    ArrayList<Pregunta> preguntas;
    private MediaPlayer mpSucess;
    private MediaPlayer mpFail;
    private MediaPlayer mpMusic;

    long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Random random = new Random();

        btRespuesta1 = findViewById(R.id.button_respuesta_1);
        btRespuesta2 = findViewById(R.id.button_respuesta_2);
        btRespuesta3 = findViewById(R.id.button_respuesta_3);
        btRespuesta4 = findViewById(R.id.button_respuesta_4);
        tvPregunta = findViewById(R.id.tv_pregunta);
        tvMarcador = findViewById(R.id.tv_marcador);
        tvTimer = findViewById(R.id.tv_timer);
        totalQuestions = -1;
        correctQuestions = 0;
        mpSucess = MediaPlayer.create(this, R.raw.success);
        mpFail = MediaPlayer.create(this, R.raw.fail);
        if (random.nextBoolean()) {
            mpMusic = MediaPlayer.create(this, R.raw.quiz1);
        } else {
            mpMusic = MediaPlayer.create(this, R.raw.quiz2);
        }
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
        mpMusic.start();
    }

    public void showPopup(String tittle, String message){
        mpMusic.setPlaybackParams(mpMusic.getPlaybackParams().setSpeed(1.0f));
        mpMusic.pause();
        timerHandler.removeCallbacks(timerRunnable);
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(tittle);
        dialogBuilder.setMessage(message);
        dialog = dialogBuilder.create();
        dialog.setButton(Dialog.BUTTON_POSITIVE, "Entendido", (dialogInterface, i) -> {
            if (Objects.equals(tittle, "¡Correcto!")){
                correctQuestions+=1;
                tvMarcador.setText(correctQuestions + "/10");
            }
            mpMusic.start();
            nextQuestion();
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void nextQuestion(){
        totalQuestions += 1;
        if (totalQuestions<10){
            tvTimer.setTextColor(Color.parseColor("#808080"));
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
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
                    mpSucess.start();
                }
                else {
                    mpFail.start();
                }
                showPopup(message, respuestas.get(0).getJustificacion());
            });

            btRespuesta2.setOnClickListener(v -> {
                String message = "Incorrecto :(";
                if (respuestas.get(1).isCorrecta()){
                    message = "¡Correcto!";
                    mpSucess.start();
                }
                else {
                    mpFail.start();
                }
                showPopup(message, respuestas.get(1).getJustificacion());
            });

            btRespuesta3.setOnClickListener(v -> {
                String message = "Incorrecto :(";
                if (respuestas.get(2).isCorrecta()){
                    message = "¡Correcto!";
                    mpSucess.start();
                }
                else {
                    mpFail.start();
                }
                showPopup(message, respuestas.get(2).getJustificacion());
            });

            btRespuesta4.setOnClickListener(v -> {
                String message = "Incorrecto :(";
                if (respuestas.get(3).isCorrecta()){
                    message = "¡Correcto!";
                    mpSucess.start();
                }
                else {
                    mpFail.start();
                }
                showPopup(message, respuestas.get(3).getJustificacion());
            });
        }
        else {
            endGame();
        }
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = 30 - ((int) (millis / 1000));
            if (seconds<0){
                mpMusic.pause();
                mpFail.start();
                showPopup("Tarde D:", "Tardaste mucho en responder la pregunta... ¡Deja de buscarlas en el reglamento!");
            }
            else {
                if (seconds<=10){
                    mpMusic.setPlaybackParams(mpMusic.getPlaybackParams().setSpeed(1.2f));
                    tvTimer.setTextColor(Color.parseColor("#FF0000"));
                }
                tvTimer.setText(String.format("%02d", seconds));
                timerHandler.postDelayed(this, 500);
            }
        }
    };

    public void endGame(){
        String tittle, message, nextButton;
        if (correctQuestions<=3){
            tittle = "Muy mal... Sacaste un " + correctQuestions;
            message = "Tienes que seguir leyendo el reglamento, tu puntaje fue muyyyyyy bajo.";
            nextButton = "Prometo mejorar";
        }
        else if (correctQuestions<=6){
            tittle = "Puede mejorar... Sacaste un " + correctQuestions;
            message = "Vas en buen camino, al menos ya sabes el estudiante promedio de la Universidad";
            nextButton = "¡Se puede mejorar!";
        }
        else if (correctQuestions<=9){
            tittle = "¡Muy bien!";
            message = "Sacaste un " + correctQuestions + " conoces bastante del reglamente de la Universidad, si te metes en problemas ya es culpa tuya.";
            nextButton = "Ya no soy inocente por ignorancia";
        }
        else if (correctQuestions==10){
            tittle = "PERFECTO";
            message = "MAGNIFICO, SACASTE UN " + correctQuestions + "... Espero no hayas hecho trampa... CELEBREMOSLO (Pero sin alcohol, dentro de la universidad no se puede)";
            nextButton = "SOY INCREIBLEEEEE";
        }
        else {
            tittle = "HACKER";
            message = "COMO LOGRASTE SACAR " + correctQuestions + "...";
            nextButton = "Me averguenzo de mis acciones";
        }
        mpMusic.pause();
        timerHandler.removeCallbacks(timerRunnable);
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(tittle);
        dialogBuilder.setMessage(message);
        dialog = dialogBuilder.create();
        dialog.setButton(Dialog.BUTTON_POSITIVE, nextButton, (dialogInterface, i) -> {
            finish();
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            mpMusic.stop();
            timerHandler.removeCallbacks(timerRunnable);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presiona atrás de nuevo para salir.", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}