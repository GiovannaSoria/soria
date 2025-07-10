package com.examen.soria.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DenominacionBilletesDTO {
    
    @NotBlank(message = "El tipo de billete es requerido")
    @Pattern(regexp = "UNO|CINCO|DIEZ|VEINTE|CINCUENTA|CIEN", 
             message = "El tipo de billete debe ser: UNO, CINCO, DIEZ, VEINTE, CINCUENTA o CIEN")
    private String billete;
    
    @NotNull(message = "La cantidad de billetes es requerida")
    @Min(value = 0, message = "La cantidad de billetes no puede ser negativa")
    private Integer cantidadBilletes;
    
    private BigDecimal monto;
} 