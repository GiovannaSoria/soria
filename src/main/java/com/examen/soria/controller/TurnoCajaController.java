package com.examen.soria.controller;

import com.examen.soria.controller.dto.AlertaCierreDTO;
import com.examen.soria.controller.dto.CerrarTurnoDTO;
import com.examen.soria.controller.dto.IniciarTurnoDTO;
import com.examen.soria.controller.dto.TurnoCajaDTO;
import com.examen.soria.controller.mapper.TurnoCajaMapper;
import com.examen.soria.exception.MontoNoCoincideException;
import com.examen.soria.exception.TurnoNotFoundException;
import com.examen.soria.exception.TurnoYaAbierto;
import com.examen.soria.model.TurnoCaja;
import com.examen.soria.service.TurnoCajaService;
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

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/v1/turnos")
@Tag(name = "Turnos de Caja", description = "API para gestión de turnos de caja")
@Slf4j
public class TurnoCajaController {

    private final TurnoCajaService turnoCajaService;
    private final TurnoCajaMapper turnoCajaMapper;

    public TurnoCajaController(TurnoCajaService turnoCajaService, TurnoCajaMapper turnoCajaMapper) {
        this.turnoCajaService = turnoCajaService;
        this.turnoCajaMapper = turnoCajaMapper;
    }

    @PostMapping("/iniciar")
    @Operation(summary = "Iniciar turno", description = "Inicia un nuevo turno de caja registrando el dinero inicial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno iniciado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un turno abierto para esta caja y cajero")
    })
    public ResponseEntity<TurnoCajaDTO> iniciarTurno(@Valid @RequestBody IniciarTurnoDTO iniciarTurnoDTO) {
        try {
            log.info("Iniciando turno para caja: {} y cajero: {}", 
                    iniciarTurnoDTO.getCodigoCaja(), iniciarTurnoDTO.getCodigoCajero());
            
            TurnoCaja turno = turnoCajaService.iniciarTurno(iniciarTurnoDTO);
            TurnoCajaDTO turnoDTO = turnoCajaMapper.toDTO(turno);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(turnoDTO);
        } catch (TurnoYaAbierto e) {
            log.error("Error al iniciar turno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/cerrar")
    @Operation(summary = "Cerrar turno", description = "Cierra un turno de caja y genera alerta si los montos no coinciden")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno cerrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado"),
            @ApiResponse(responseCode = "409", description = "El turno ya está cerrado")
    })
    public ResponseEntity<AlertaCierreDTO> cerrarTurno(@Valid @RequestBody CerrarTurnoDTO cerrarTurnoDTO) {
        try {
            log.info("Cerrando turno: {}", cerrarTurnoDTO.getCodigoTurno());
            
            AlertaCierreDTO alertaCierre = turnoCajaService.cerrarTurno(cerrarTurnoDTO);
            
            return ResponseEntity.ok(alertaCierre);
        } catch (TurnoNotFoundException e) {
            log.error("Error al cerrar turno: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Error al cerrar turno: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{codigoTurno}")
    @Operation(summary = "Obtener turno por código", description = "Obtiene los detalles de un turno específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    })
    public ResponseEntity<TurnoCajaDTO> obtenerTurnoPorCodigo(
            @Parameter(description = "Código del turno", example = "CAJ01-USU01-20250709")
            @PathVariable String codigoTurno) {
        try {
            TurnoCaja turno = turnoCajaService.obtenerTurnoPorCodigo(codigoTurno);
            TurnoCajaDTO turnoDTO = turnoCajaMapper.toDTO(turno);
            return ResponseEntity.ok(turnoDTO);
        } catch (TurnoNotFoundException e) {
            log.error("Turno no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener turnos paginados", description = "Obtiene lista paginada de turnos")
    @ApiResponse(responseCode = "200", description = "Lista de turnos obtenida exitosamente")
    public ResponseEntity<Page<TurnoCajaDTO>> obtenerTurnos(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<TurnoCaja> page = turnoCajaService.obtenerTurnos(pageable);
        Page<TurnoCajaDTO> dtoPage = page.map(turnoCajaMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/abiertos")
    @Operation(summary = "Obtener turnos abiertos", description = "Obtiene lista de turnos que están actualmente abiertos")
    @ApiResponse(responseCode = "200", description = "Lista de turnos abiertos obtenida exitosamente")
    public ResponseEntity<List<TurnoCajaDTO>> obtenerTurnosAbiertos() {
        List<TurnoCaja> turnos = turnoCajaService.obtenerTurnosAbiertos();
        List<TurnoCajaDTO> turnosDTO = turnos.stream()
                .map(turnoCajaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(turnosDTO);
    }

    @ExceptionHandler({TurnoNotFoundException.class})
    public ResponseEntity<String> handleTurnoNotFound(TurnoNotFoundException e) {
        log.error("Turno no encontrado: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({TurnoYaAbierto.class})
    public ResponseEntity<String> handleTurnoYaAbierto(TurnoYaAbierto e) {
        log.error("Turno ya abierto: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler({MontoNoCoincideException.class})
    public ResponseEntity<String> handleMontoNoCoincide(MontoNoCoincideException e) {
        log.error("Monto no coincide: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
} 