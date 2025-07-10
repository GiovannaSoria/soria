package com.examen.soria.controller;

import com.examen.soria.controller.dto.ProcesarTransaccionDTO;
import com.examen.soria.controller.dto.TransaccionTurnoDTO;
import com.examen.soria.controller.mapper.TransaccionTurnoMapper;
import com.examen.soria.exception.TurnoNotFoundException;
import com.examen.soria.exception.SaldoInsuficienteException;
import com.examen.soria.model.TransaccionTurno;
import com.examen.soria.service.TransaccionTurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1/transacciones")
@Tag(name = "Transacciones", description = "API para gestión de transacciones de turno")
@Slf4j
public class TransaccionTurnoController {

    private final TransaccionTurnoService transaccionTurnoService;
    private final TransaccionTurnoMapper transaccionTurnoMapper;

    public TransaccionTurnoController(TransaccionTurnoService transaccionTurnoService, 
                                     TransaccionTurnoMapper transaccionTurnoMapper) {
        this.transaccionTurnoService = transaccionTurnoService;
        this.transaccionTurnoMapper = transaccionTurnoMapper;
    }

    @PostMapping("/procesar")
    @Operation(summary = "Procesar transacción", description = "Procesa una transacción de depósito o retiro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción procesada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    public ResponseEntity<TransaccionTurnoDTO> procesarTransaccion(@Valid @RequestBody ProcesarTransaccionDTO transaccionDTO) {
        try {
            log.info("Procesando transacción tipo: {} para turno: {}", 
                    transaccionDTO.getTipoTransaccion(), transaccionDTO.getCodigoTurno());
            
            TransaccionTurno transaccion = transaccionTurnoService.procesarTransaccion(transaccionDTO);
            TransaccionTurnoDTO transaccionDTO1 = transaccionTurnoMapper.toDTO(transaccion);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccionDTO1);
        } catch (TurnoNotFoundException e) {
            log.error("Error al procesar transacción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/turno/{codigoTurno}")
    @Operation(summary = "Obtener transacciones por turno", description = "Obtiene todas las transacciones de un turno específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones encontradas"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    public ResponseEntity<List<TransaccionTurnoDTO>> obtenerTransaccionesPorTurno(
            @Parameter(description = "Código del turno", example = "CAJ01-USU01-20250709")
            @PathVariable String codigoTurno) {
        try {
            List<TransaccionTurno> transacciones = transaccionTurnoService.obtenerTransaccionesPorTurno(codigoTurno);
            List<TransaccionTurnoDTO> transaccionesDTO = transacciones.stream()
                    .map(transaccionTurnoMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(transaccionesDTO);
        } catch (Exception e) {
            log.error("Error al obtener transacciones: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transacción por ID", description = "Obtiene los detalles de una transacción específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción encontrada"),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada")
    })
    public ResponseEntity<TransaccionTurnoDTO> obtenerTransaccionPorId(
            @Parameter(description = "ID de la transacción")
            @PathVariable String id) {
        try {
            TransaccionTurno transaccion = transaccionTurnoService.obtenerTransaccionPorId(id);
            TransaccionTurnoDTO transaccionDTO = transaccionTurnoMapper.toDTO(transaccion);
            return ResponseEntity.ok(transaccionDTO);
        } catch (TurnoNotFoundException e) {
            log.error("Transacción no encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener transacciones paginadas", description = "Obtiene lista paginada de transacciones")
    @ApiResponse(responseCode = "200", description = "Lista de transacciones obtenida exitosamente")
    public ResponseEntity<Page<TransaccionTurnoDTO>> obtenerTodasTransacciones(
            @PageableDefault(size = 50) Pageable pageable) {
        Page<TransaccionTurno> page = transaccionTurnoService.obtenerTodasTransacciones(pageable);
        Page<TransaccionTurnoDTO> dtoPage = page.map(transaccionTurnoMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @ExceptionHandler({TurnoNotFoundException.class})
    public ResponseEntity<String> handleTurnoNotFound(TurnoNotFoundException e) {
        log.error("Recurso no encontrado: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({SaldoInsuficienteException.class})
    public ResponseEntity<String> handleSaldoInsuficiente(SaldoInsuficienteException e) {
        log.error("Saldo insuficiente: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
} 