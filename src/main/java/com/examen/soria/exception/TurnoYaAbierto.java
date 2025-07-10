package com.examen.soria.exception;

public class TurnoYaAbierto extends RuntimeException {

    private final String codigoCaja;
    private final String codigoCajero;

    public TurnoYaAbierto(String codigoCaja, String codigoCajero) {
        super();
        this.codigoCaja = codigoCaja;
        this.codigoCajero = codigoCajero;
    }

    @Override
    public String getMessage() {
        return "Ya existe un turno abierto para la caja: " + codigoCaja + " y cajero: " + codigoCajero;
    }
} 