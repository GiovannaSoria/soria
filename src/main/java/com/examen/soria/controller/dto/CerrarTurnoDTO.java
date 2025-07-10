package com.examen.soria.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CerrarTurnoDTO {
    
    @NotBlank(message = "El código de turno es requerido")
    private String codigoTurno;
    
    @NotEmpty(message = "Las denominaciones finales son requeridas")
    @Valid
    private List<DenominacionBilletesDTO> denominacionesFinales;
} 