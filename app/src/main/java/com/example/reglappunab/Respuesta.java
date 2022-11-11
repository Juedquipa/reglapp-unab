package com.example.reglappunab;

public class Respuesta {
    private String respuesta;
    private boolean correcta;
    private String justificacion;

    public Respuesta(String respuesta, String justificacion) {
        this.respuesta = respuesta;
        this.justificacion = justificacion;
        this.correcta = false;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public boolean isCorrecta() {
        return correcta;
    }

    public void setCorrecta(boolean correcta) {
        this.correcta = correcta;
    }

    public String getJustificacion() {
        return justificacion;
    }
}
