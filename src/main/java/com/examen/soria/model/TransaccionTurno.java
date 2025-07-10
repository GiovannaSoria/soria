package com.examen.soria.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "transacciones_turno")
public class TransaccionTurno {
    
    @Id
    private String id;
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private String tipoTransaccion;
    private BigDecimal montoTotal;
    private LocalDateTime fechaTransaccion;
    private List<DenominacionBilletes> denominaciones;

    public TransaccionTurno(String codigoCaja, String codigoCajero, String codigoTurno) {
        this.codigoCaja = codigoCaja;
        this.codigoCajero = codigoCajero;
        this.codigoTurno = codigoTurno;
        this.denominaciones = new ArrayList<>();
        this.montoTotal = BigDecimal.ZERO;
        this.fechaTransaccion = LocalDateTime.now();
    }

    public void calcularMontoTotal() {
        this.montoTotal = this.denominaciones.stream()
                .map(DenominacionBilletes::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TransaccionTurno that = (TransaccionTurno) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 