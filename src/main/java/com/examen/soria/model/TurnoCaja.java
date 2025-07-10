package com.examen.soria.model;

import com.examen.soria.constants.BancoConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "turnos_caja")
public class TurnoCaja {
    
    @Id
    private String id;
    private String codigoCaja;
    private String codigoCajero;
    private String codigoTurno;
    private LocalDateTime inicioTurno;
    private BigDecimal montoInicial;
    private LocalDateTime finTurno;
    private BigDecimal montoFinal;
    private String estado;
    private List<DenominacionBilletes> denominacionesIniciales;
    private List<DenominacionBilletes> denominacionesFinales;

    public TurnoCaja(String codigoCaja, String codigoCajero) {
        this.codigoCaja = codigoCaja;
        this.codigoCajero = codigoCajero;
        this.inicioTurno = LocalDateTime.now();
        this.estado = BancoConstants.ESTADO_ABIERTO;
        this.denominacionesIniciales = new ArrayList<>();
        this.denominacionesFinales = new ArrayList<>();
        this.montoInicial = BigDecimal.ZERO;
        this.montoFinal = BigDecimal.ZERO;
        this.generarCodigoTurno();
    }

    private void generarCodigoTurno() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fecha = this.inicioTurno.format(formatter);
        this.codigoTurno = this.codigoCaja + "-" + this.codigoCajero + "-" + fecha;
    }

    public void calcularMontoInicial() {
        this.montoInicial = this.denominacionesIniciales.stream()
                .map(DenominacionBilletes::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void calcularMontoFinal() {
        this.montoFinal = this.denominacionesFinales.stream()
                .map(DenominacionBilletes::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cerrarTurno() {
        this.finTurno = LocalDateTime.now();
        this.estado = BancoConstants.ESTADO_CERRADO;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TurnoCaja that = (TurnoCaja) obj;
        return codigoTurno != null && codigoTurno.equals(that.codigoTurno);
    }

    @Override
    public int hashCode() {
        return codigoTurno != null ? codigoTurno.hashCode() : 0;
    }
} 