package com.examen.soria.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TransaccionTurnoDTO {
    
    private String id;
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private String tipoTransaccion;
    private BigDecimal montoTotal;
    private LocalDateTime fechaTransaccion;
    private List<DenominacionBilletesDTO> denominaciones;
} 