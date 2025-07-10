package com.examen.soria.constants;

import java.math.BigDecimal;

public final class BancoConstants {
    
    private BancoConstants() {
    }
    
    public static final String ESTADO_ABIERTO = "ABIERTO";
    public static final String ESTADO_CERRADO = "CERRADO";
    
    public static final String TIPO_INICIO = "INICIO";
    public static final String TIPO_RETIRO = "RETIRO";
    public static final String TIPO_DEPOSITO = "DEPOSITO";
    public static final String TIPO_CIERRE = "CIERRE";
    
    public static final String DENOM_UNO = "UNO";
    public static final String DENOM_CINCO = "CINCO";
    public static final String DENOM_DIEZ = "DIEZ";
    public static final String DENOM_VEINTE = "VEINTE";
    public static final String DENOM_CINCUENTA = "CINCUENTA";
    public static final String DENOM_CIEN = "CIEN";
    
    public static BigDecimal obtenerValorDenominacion(String denominacion) {
        switch (denominacion) {
            case DENOM_UNO:
                return new BigDecimal("1");
            case DENOM_CINCO:
                return new BigDecimal("5");
            case DENOM_DIEZ:
                return new BigDecimal("10");
            case DENOM_VEINTE:
                return new BigDecimal("20");
            case DENOM_CINCUENTA:
                return new BigDecimal("50");
            case DENOM_CIEN:
                return new BigDecimal("100");
            default:
                throw new IllegalArgumentException("Denominación no válida: " + denominacion);
        }
    }
} 