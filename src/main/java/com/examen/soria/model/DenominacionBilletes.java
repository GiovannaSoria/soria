package com.examen.soria.model;

import com.examen.soria.constants.BancoConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DenominacionBilletes {
    
    private String billete;
    private Integer cantidadBilletes;
    private BigDecimal monto;

    public DenominacionBilletes(String billete) {
        this.billete = billete;
        this.cantidadBilletes = 0;
        this.monto = BigDecimal.ZERO;
    }

    public void calcularMonto() {
        if (this.billete != null && this.cantidadBilletes != null) {
            BigDecimal valorBillete = BancoConstants.obtenerValorDenominacion(this.billete);
            this.monto = valorBillete.multiply(new BigDecimal(this.cantidadBilletes));
        } else {
            this.monto = BigDecimal.ZERO;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DenominacionBilletes that = (DenominacionBilletes) obj;
        return billete != null && billete.equals(that.billete);
    }

    @Override
    public int hashCode() {
        return billete != null ? billete.hashCode() : 0;
    }
} 