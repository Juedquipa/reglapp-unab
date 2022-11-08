package com.example.reglappunab;

import java.util.ArrayList;

public class Pregunta {
    private String pregunta;
    private ArrayList<Respuesta> respuestas;

    public Pregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuestas(ArrayList<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public String getPregunta() {
        return pregunta;
    }

    public ArrayList<Respuesta> getRespuestas() {
        return respuestas;
    }
}
