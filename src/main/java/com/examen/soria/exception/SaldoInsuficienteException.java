package com.examen.soria.exception;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {

    private final BigDecimal saldoActual;
    private final BigDecimal montoRetiro;

    public SaldoInsuficienteException(BigDecimal saldoActual, BigDecimal montoRetiro) {
        super();
        this.saldoActual = saldoActual;
        this.montoRetiro = montoRetiro;
    }

    @Override
    public String getMessage() {
        return "Saldo insuficiente. Saldo disponible: " + saldoActual + ", monto solicitado: " + montoRetiro;
    }
} 