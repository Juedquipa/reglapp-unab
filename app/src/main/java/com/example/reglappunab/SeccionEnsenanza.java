package com.example.reglappunab;

import java.io.Serializable;

public class SeccionEnsenanza implements Serializable {
    private String nombre;
    private String content;

    public SeccionEnsenanza(String nombre, String content) {
        this.nombre = nombre;
        this.content = content;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
