package com.examen.soria.controller.mapper;

import com.examen.soria.controller.dto.TurnoCajaDTO;
import com.examen.soria.model.TurnoCaja;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {DenominacionBilletesMapper.class}
)
public interface TurnoCajaMapper {

    TurnoCajaDTO toDTO(TurnoCaja model);
    
    TurnoCaja toModel(TurnoCajaDTO dto);
} 