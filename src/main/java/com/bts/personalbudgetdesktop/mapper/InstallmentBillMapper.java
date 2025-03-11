package com.bts.personalbudgetdesktop.mapper;

import com.bts.personalbudgetdesktop.model.InstallmentBill;
import com.bts.personalbudgetdesktop.model.InstallmentBillDTO;
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

    Set<InstallmentBillDTO> modelListToDtoList(Set<InstallmentBill> installmentBillList);

    @Named("mapCode")
    default UUID mapCode(String code) {
        return (code == null || code.isEmpty()) ? UUID.randomUUID() : UUID.fromString(code);
    }
}
