package com.examen.soria.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "denominacion")
public class Denominacion {

    @EmbeddedId
    private DenominacionId id;

    @Column(name = "cantidad_billetes", nullable = false)
    private Integer cantidadBilletes;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Version
    private Long version;

    @MapsId("turnoId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", nullable = false)
    private TransaccionTurno transaccion;

    public Denominacion() {
    }

    public Denominacion(DenominacionId id) {
        this.id = id;
    }

    public DenominacionId getId() {
        return id;
    }

    public void setId(DenominacionId id) {
        this.id = id;
    }

    public Integer getCantidadBilletes() {
        return cantidadBilletes;
    }

    public void setCantidadBilletes(Integer cantidadBilletes) {
        this.cantidadBilletes = cantidadBilletes;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public TransaccionTurno getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(TransaccionTurno transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        Denominacion other = (Denominacion) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Denominacion [id=" + id + ", cantidadBilletes=" + cantidadBilletes + ", monto=" + monto
                + ", version=" + version + "]";
    }
} 