package com.examen.soria.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProcesarTransaccionDTO {
    
    @NotBlank(message = "El código de turno es requerido")
    private String codigoTurno;
    
    @NotBlank(message = "El tipo de transacción es requerido")
    @Pattern(regexp = "RETIRO|DEPOSITO", 
             message = "El tipo de transacción debe ser: RETIRO o DEPOSITO")
    private String tipoTransaccion;
    
    @NotEmpty(message = "Las denominaciones son requeridas")
    @Valid
    private List<DenominacionBilletesDTO> denominaciones;
} 