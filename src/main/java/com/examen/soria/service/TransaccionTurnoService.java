package com.examen.soria.service;

import com.examen.soria.constants.BancoConstants;
import com.examen.soria.controller.dto.ProcesarTransaccionDTO;
import com.examen.soria.exception.TurnoNotFoundException;
import com.examen.soria.repository.TransaccionTurnoRepository;
import com.examen.soria.repository.TurnoCajaRepository;
import com.examen.soria.exception.SaldoInsuficienteException;
import com.examen.soria.model.DenominacionBilletes;
import com.examen.soria.model.TransaccionTurno;
import com.examen.soria.model.TurnoCaja;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@Slf4j
public class TransaccionTurnoService {
    
    private final TransaccionTurnoRepository transaccionTurnoRepository;
    private final TurnoCajaRepository turnoCajaRepository;

    public TransaccionTurnoService(TransaccionTurnoRepository transaccionTurnoRepository,
                                   TurnoCajaRepository turnoCajaRepository) {
        this.transaccionTurnoRepository = transaccionTurnoRepository;
        this.turnoCajaRepository = turnoCajaRepository;
    }
    
    public TransaccionTurno procesarTransaccion(ProcesarTransaccionDTO transaccionDTO) {
        log.info("Procesando transacción tipo: {} para turno: {}", 
                transaccionDTO.getTipoTransaccion(), transaccionDTO.getCodigoTurno());
        
        TurnoCaja turno = turnoCajaRepository.findByCodigoTurno(transaccionDTO.getCodigoTurno())
                .orElseThrow(() -> new TurnoNotFoundException(transaccionDTO.getCodigoTurno(), "TurnoCaja"));

        if (!BancoConstants.ESTADO_ABIERTO.equals(turno.getEstado())) {
            throw new RuntimeException("No se puede procesar transacciones en un turno cerrado");
        }

        TransaccionTurno transaccion = new TransaccionTurno();
        transaccion.setCodigoTurno(transaccionDTO.getCodigoTurno());
        transaccion.setTipoTransaccion(transaccionDTO.getTipoTransaccion());
        
        List<DenominacionBilletes> denominaciones = transaccionDTO.getDenominaciones().stream()
                .map(dto -> {
                    DenominacionBilletes denominacion = new DenominacionBilletes(dto.getBillete());
                    denominacion.setCantidadBilletes(dto.getCantidadBilletes());
                    denominacion.calcularMonto();
                    return denominacion;
                })
                .collect(Collectors.toList());
        
        transaccion.setDenominaciones(denominaciones);
        transaccion.calcularMontoTotal();

        if (BancoConstants.TIPO_RETIRO.equals(transaccionDTO.getTipoTransaccion())) {
            BigDecimal saldoActual = calcularSaldoActual(turno);
            if (transaccion.getMontoTotal().compareTo(saldoActual) > 0) {
                throw new SaldoInsuficienteException(saldoActual, transaccion.getMontoTotal());
            }
        }
        
        String[] partes = transaccionDTO.getCodigoTurno().split("-");
        if (partes.length >= 2) {
            transaccion.setCodigoCaja(partes[0]);
            transaccion.setCodigoCajero(partes[1]);
        }
        
        TransaccionTurno transaccionGuardada = transaccionTurnoRepository.save(transaccion);
        
        log.info("Transacción procesada exitosamente con ID: {}", transaccionGuardada.getId());
        return transaccionGuardada;
    }
    
    public TransaccionTurno crearTransaccionInicial(TurnoCaja turno) {
        log.info("Creando transacción inicial para turno: {}", turno.getCodigoTurno());
        
        TransaccionTurno transaccion = new TransaccionTurno(
                turno.getCodigoCaja(), 
                turno.getCodigoCajero(), 
                turno.getCodigoTurno()
        );
        
        transaccion.setTipoTransaccion(BancoConstants.TIPO_INICIO);
        transaccion.setDenominaciones(turno.getDenominacionesIniciales());
        transaccion.calcularMontoTotal();
        
        return transaccionTurnoRepository.save(transaccion);
    }
    
    public TransaccionTurno crearTransaccionCierre(TurnoCaja turno) {
        log.info("Creando transacción de cierre para turno: {}", turno.getCodigoTurno());
        
        TransaccionTurno transaccion = new TransaccionTurno(
                turno.getCodigoCaja(), 
                turno.getCodigoCajero(), 
                turno.getCodigoTurno()
        );
        
        transaccion.setTipoTransaccion(BancoConstants.TIPO_CIERRE);
        transaccion.setDenominaciones(turno.getDenominacionesFinales());
        transaccion.calcularMontoTotal();
        
        return transaccionTurnoRepository.save(transaccion);
    }
    
    public List<TransaccionTurno> obtenerTransaccionesPorTurno(String codigoTurno) {
        return transaccionTurnoRepository.findByCodigoTurno(codigoTurno);
    }
    
    public List<TransaccionTurno> obtenerTodasTransacciones() {
        return transaccionTurnoRepository.findAll();
    }
    
    public TransaccionTurno obtenerTransaccionPorId(String id) {
        return transaccionTurnoRepository.findById(id)
                .orElseThrow(() -> new TurnoNotFoundException(id, "TransaccionTurno"));
    }

    private BigDecimal calcularSaldoActual(TurnoCaja turno) {
        BigDecimal saldo = turno.getMontoInicial();
        List<TransaccionTurno> transacciones = transaccionTurnoRepository.findByCodigoTurno(turno.getCodigoTurno());

        for (TransaccionTurno t : transacciones) {
            if (BancoConstants.TIPO_DEPOSITO.equals(t.getTipoTransaccion())) {
                saldo = saldo.add(t.getMontoTotal());
            } else if (BancoConstants.TIPO_RETIRO.equals(t.getTipoTransaccion())) {
                saldo = saldo.subtract(t.getMontoTotal());
            }
        }
        return saldo;
    }

    public Page<TransaccionTurno> obtenerTodasTransacciones(Pageable pageable) {
        return transaccionTurnoRepository.findAll(pageable);
    }
} 