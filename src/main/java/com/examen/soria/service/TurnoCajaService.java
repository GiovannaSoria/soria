package com.examen.soria.service;

import com.examen.soria.constants.BancoConstants;
import com.examen.soria.controller.dto.AlertaCierreDTO;
import com.examen.soria.controller.dto.CerrarTurnoDTO;
import com.examen.soria.controller.dto.IniciarTurnoDTO;
import com.examen.soria.exception.TurnoNotFoundException;
import com.examen.soria.exception.TurnoYaAbierto;
import com.examen.soria.model.DenominacionBilletes;
import com.examen.soria.model.TransaccionTurno;
import com.examen.soria.model.TurnoCaja;
import com.examen.soria.repository.TurnoCajaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TurnoCajaService {
    
    private final TurnoCajaRepository turnoCajaRepository;
    private final TransaccionTurnoService transaccionTurnoService;
    
    public TurnoCajaService(TurnoCajaRepository turnoCajaRepository, TransaccionTurnoService transaccionTurnoService) {
        this.turnoCajaRepository = turnoCajaRepository;
        this.transaccionTurnoService = transaccionTurnoService;
    }
    
    public TurnoCaja iniciarTurno(IniciarTurnoDTO iniciarTurnoDTO) {
        log.info("Iniciando turno para caja: {} y cajero: {}", iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero());
        

        if (turnoCajaRepository.existsByCodigoCajaAndCodigoCajeroAndEstado(
                iniciarTurnoDTO.getCodigoCaja(), 
                iniciarTurnoDTO.getCodigoCajero(), 
                BancoConstants.ESTADO_ABIERTO)) {
            throw new TurnoYaAbierto(iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero());
        }
        
        TurnoCaja nuevoTurno = new TurnoCaja(iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero());
        
        List<DenominacionBilletes> denominaciones = iniciarTurnoDTO.getDenominacionesIniciales().stream()
                .map(dto -> {
                    DenominacionBilletes denominacion = new DenominacionBilletes(dto.getBillete());
                    denominacion.setCantidadBilletes(dto.getCantidadBilletes());
                    denominacion.calcularMonto();
                    return denominacion;
                })
                .collect(Collectors.toList());
        
        nuevoTurno.setDenominacionesIniciales(denominaciones);
        nuevoTurno.calcularMontoInicial();
        
        TurnoCaja turnoGuardado = turnoCajaRepository.save(nuevoTurno);
        
        transaccionTurnoService.crearTransaccionInicial(turnoGuardado);
        
        log.info("Turno iniciado exitosamente con código: {}", turnoGuardado.getCodigoTurno());
        return turnoGuardado;
    }
    
    public AlertaCierreDTO cerrarTurno(CerrarTurnoDTO cerrarTurnoDTO) {
        log.info("Cerrando turno: {}", cerrarTurnoDTO.getCodigoTurno());
        
        TurnoCaja turno = turnoCajaRepository.findByCodigoTurno(cerrarTurnoDTO.getCodigoTurno())
                .orElseThrow(() -> new TurnoNotFoundException(cerrarTurnoDTO.getCodigoTurno(), "TurnoCaja"));
        
        if (BancoConstants.ESTADO_CERRADO.equals(turno.getEstado())) {
            throw new RuntimeException("El turno ya está cerrado");
        }
        
        List<DenominacionBilletes> denominacionesFinales = cerrarTurnoDTO.getDenominacionesFinales().stream()
                .map(dto -> {
                    DenominacionBilletes denominacion = new DenominacionBilletes(dto.getBillete());
                    denominacion.setCantidadBilletes(dto.getCantidadBilletes());
                    denominacion.calcularMonto();
                    return denominacion;
                })
                .collect(Collectors.toList());
        
        turno.setDenominacionesFinales(denominacionesFinales);
        turno.calcularMontoFinal();
        
        BigDecimal montoCalculado = calcularMontoEsperado(turno);
        
        transaccionTurnoService.crearTransaccionCierre(turno);
        
        turno.cerrarTurno();
        turnoCajaRepository.save(turno);
        
        log.info("Turno cerrado. Monto calculado: {}, Monto ingresado: {}", montoCalculado, turno.getMontoFinal());
        
        return new AlertaCierreDTO(turno.getCodigoTurno(), montoCalculado, turno.getMontoFinal());
    }
    
    private BigDecimal calcularMontoEsperado(TurnoCaja turno) {
        List<TransaccionTurno> transacciones = transaccionTurnoService.obtenerTransaccionesPorTurno(turno.getCodigoTurno());
        
        BigDecimal montoEsperado = turno.getMontoInicial();
        
        for (TransaccionTurno transaccion : transacciones) {
            if (BancoConstants.TIPO_DEPOSITO.equals(transaccion.getTipoTransaccion())) {
                montoEsperado = montoEsperado.add(transaccion.getMontoTotal());
            } else if (BancoConstants.TIPO_RETIRO.equals(transaccion.getTipoTransaccion())) {
                montoEsperado = montoEsperado.subtract(transaccion.getMontoTotal());
            }
        }
        
        return montoEsperado;
    }
    
    public TurnoCaja obtenerTurnoPorCodigo(String codigoTurno) {
        return turnoCajaRepository.findByCodigoTurno(codigoTurno)
                .orElseThrow(() -> new TurnoNotFoundException(codigoTurno, "TurnoCaja"));
    }
    
    public List<TurnoCaja> obtenerTurnosAbiertos() {
        return turnoCajaRepository.findByEstado(BancoConstants.ESTADO_ABIERTO);
    }
    
    public List<TurnoCaja> obtenerTodosTurnos() {
        return turnoCajaRepository.findAll();
    }

    // Paginación y filtros básicos
    public Page<TurnoCaja> obtenerTurnos(Pageable pageable) {
        return turnoCajaRepository.findAll(pageable);
    }
} 