package com.examen.soria.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TurnoCajaDTO {
    
    private String id;
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private LocalDateTime inicioTurno;
    private BigDecimal montoInicial;
    private LocalDateTime finTurno;
    private BigDecimal montoFinal;
    private String estado;
    private List<DenominacionBilletesDTO> denominacionesIniciales;
    private List<DenominacionBilletesDTO> denominacionesFinales;
} 