package com.examen.soria.exception;

import java.math.BigDecimal;

public class MontoNoCoincideException extends RuntimeException {

    private final BigDecimal montoCalculado;
    private final BigDecimal montoIngresado;

    public MontoNoCoincideException(BigDecimal montoCalculado, BigDecimal montoIngresado) {
        super();
        this.montoCalculado = montoCalculado;
        this.montoIngresado = montoIngresado;
    }

    @Override
    public String getMessage() {
        return "El monto ingresado (" + montoIngresado + ") no coincide con el monto calculado (" + montoCalculado + ")";
    }
} 