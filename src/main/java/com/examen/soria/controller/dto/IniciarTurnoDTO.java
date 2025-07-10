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
public class IniciarTurnoDTO {
    
    @NotBlank(message = "El código de caja es requerido")
    @Pattern(regexp = "^CAJ\\d{2}$", message = "El código de caja debe seguir el formato CAJ## (ej: CAJ01)")
    private String codigoCaja;
    
    @NotBlank(message = "El código de cajero es requerido")
    @Pattern(regexp = "^USU\\d{2}$", message = "El código de cajero debe seguir el formato USU## (ej: USU01)")
    private String codigoCajero;
    
    @NotEmpty(message = "Las denominaciones iniciales son requeridas")
    @Valid
    private List<DenominacionBilletesDTO> denominacionesIniciales;
} 