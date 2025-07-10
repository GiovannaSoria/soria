package com.examen.soria.exception;

public class TurnoNotFoundException extends RuntimeException {

    private final String data;
    private final String entity;

    public TurnoNotFoundException(String data, String entity) {
        super();
        this.data = data;
        this.entity = entity;
    }

    @Override
    public String getMessage() {
        return "No se encontró ninguna coincidencia para: " + this.entity + ", con el dato: " + data;
    }
} 