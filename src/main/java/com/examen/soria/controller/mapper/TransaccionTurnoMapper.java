package com.examen.soria.controller.mapper;

import com.examen.soria.controller.dto.TransaccionTurnoDTO;
import com.examen.soria.model.TransaccionTurno;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {DenominacionBilletesMapper.class}
)
public interface TransaccionTurnoMapper {

    TransaccionTurnoDTO toDTO(TransaccionTurno model);
    
    TransaccionTurno toModel(TransaccionTurnoDTO dto);
} 