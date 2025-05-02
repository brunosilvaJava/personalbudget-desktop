package com.bts.personalbudgetdesktop.mapper;

import com.bts.personalbudgetdesktop.client.personalbudgetapi.FixedBillRequest;
import com.bts.personalbudgetdesktop.client.personalbudgetapi.FixedBillResponse;
import com.bts.personalbudgetdesktop.client.personalbudgetapi.InstallmentBillRequest;
import com.bts.personalbudgetdesktop.client.personalbudgetapi.InstallmentBillResponse;
import com.bts.personalbudgetdesktop.model.FixedBill;
import com.bts.personalbudgetdesktop.model.InstallmentBill;
import com.bts.personalbudgetdesktop.model.InstallmentBillDTO;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InstallmentBillMapper {

    InstallmentBillMapper INSTANCE = Mappers.getMapper(InstallmentBillMapper.class);

    @Mapping(target = "code", source = "code", qualifiedByName = "mapCode")
    InstallmentBill dtoToModel(InstallmentBillDTO dto);

    InstallmentBillDTO modelToDto(InstallmentBill installmentBill);

    InstallmentBillRequest dtoToRequest(InstallmentBill installmentBill);

    InstallmentBill toModel(InstallmentBillResponse installmentBillResponse);

    List<InstallmentBill> responseToModelList(List<InstallmentBillResponse> installmentBillResponseList);

    List<InstallmentBillDTO> modelListToDtoList(List<InstallmentBill> installmentBillList);

    @Named("mapCode")
    default UUID mapCode(String code) {
        return (code == null || code.isEmpty()) ? UUID.randomUUID() : UUID.fromString(code);
    }
}
