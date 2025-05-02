package com.bts.personalbudgetdesktop.mapper;

import com.bts.personalbudgetdesktop.client.personalbudgetapi.FinancialMovementRequest;
import com.bts.personalbudgetdesktop.client.personalbudgetapi.FinancialMovementResponse;
import com.bts.personalbudgetdesktop.client.personalbudgetapi.FixedBillRequest;
import com.bts.personalbudgetdesktop.client.personalbudgetapi.FixedBillResponse;
import com.bts.personalbudgetdesktop.model.FinancialMovement;
import com.bts.personalbudgetdesktop.model.FinancialMovementDTO;
import com.bts.personalbudgetdesktop.model.FixedBill;
import java.util.List;
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

    List<FinancialMovementDTO> modelListToDtoList(List<FinancialMovement> financialMovementList);

    FinancialMovementRequest dtoToRequest(FinancialMovement financialMovement);

    FinancialMovement toModel(FinancialMovementResponse financialMovementResponse);

    List<FinancialMovement> responseToModelList(List<FinancialMovementResponse> financialMovementResponseList);

    @Named("mapCode")
    default UUID mapCode(String code) {
        return (code == null || code.isEmpty()) ? UUID.randomUUID() : UUID.fromString(code);
    }

}