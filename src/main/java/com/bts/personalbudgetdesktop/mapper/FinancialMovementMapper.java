package com.bts.personalbudgetdesktop.mapper;

import com.bts.personalbudgetdesktop.model.FinancialMovement;
import com.bts.personalbudgetdesktop.model.FinancialMovementDTO;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FinancialMovementMapper {

        com.bts.personalbudgetdesktop.mapper.FinancialMovementMapper INSTANCE = Mappers.getMapper(com.bts.personalbudgetdesktop.mapper.FinancialMovementMapper.class);

        @Mapping(target = "code", source = "code", qualifiedByName = "mapCode")
        FinancialMovement dtoToModel(FinancialMovementDTO dto);

        FinancialMovementDTO modelToDto(FinancialMovement financialMovement);

        Set<FinancialMovementDTO> modelListToDtoList(Set<FinancialMovement> financialMovementList);

        @Named("mapCode")
        default UUID mapCode(String code) {
            return (code == null || code.isEmpty()) ? UUID.randomUUID() : UUID.fromString(code);
        }

}
