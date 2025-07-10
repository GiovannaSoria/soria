package com.examen.soria.controller.mapper;

import com.examen.soria.controller.dto.DenominacionBilletesDTO;
import com.examen.soria.model.DenominacionBilletes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DenominacionBilletesMapper {

    DenominacionBilletesDTO toDTO(DenominacionBilletes model);
    
    DenominacionBilletes toModel(DenominacionBilletesDTO dto);
} 