package com.examen.soria.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DenominacionId implements Serializable {

    @Column(name = "turno_id", nullable = false)
    private String turnoId;

    @Column(name = "billete_valor", nullable = false)
    private BigDecimal billeteValor;

    public DenominacionId() {
    }

    public DenominacionId(String turnoId, BigDecimal billeteValor) {
        this.turnoId = turnoId;
        this.billeteValor = billeteValor;
    }

    public String getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(String turnoId) {
        this.turnoId = turnoId;
    }

    public BigDecimal getBilleteValor() {
        return billeteValor;
    }

    public void setBilleteValor(BigDecimal billeteValor) {
        this.billeteValor = billeteValor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((turnoId == null) ? 0 : turnoId.hashCode());
        result = prime * result + ((billeteValor == null) ? 0 : billeteValor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DenominacionId other = (DenominacionId) obj;
        if (turnoId == null) {
            if (other.turnoId != null)
                return false;
        } else if (!turnoId.equals(other.turnoId))
            return false;
        if (billeteValor == null) {
            if (other.billeteValor != null)
                return false;
        } else if (!billeteValor.equals(other.billeteValor))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DenominacionId [turnoId=" + turnoId + ", billeteValor=" + billeteValor + "]";
    }
} 