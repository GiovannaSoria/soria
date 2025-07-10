package com.examen.soria.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AlertaCierreDTO {
    
    private String codigoTurno;
    private BigDecimal montoCalculado;
    private BigDecimal montoIngresado;
    private BigDecimal diferencia;
    private String mensaje;
    private boolean alertaGenerada;

    public AlertaCierreDTO(String codigoTurno, BigDecimal montoCalculado, BigDecimal montoIngresado) {
        this.codigoTurno = codigoTurno;
        this.montoCalculado = montoCalculado;
        this.montoIngresado = montoIngresado;
        this.diferencia = montoIngresado.subtract(montoCalculado);
        this.alertaGenerada = !montoCalculado.equals(montoIngresado);
        this.mensaje = this.alertaGenerada ? 
            "ALERTA: El monto ingresado no coincide con el calculado" : 
            "Cierre de turno exitoso";
    }
} 