package com.micropos.delivery.mapper;

import com.micropos.delivery.model.Waybill;
import com.micropos.dto.WaybillDto;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper
public interface WaybillMapper {
    WaybillDto toWaybillDto(Waybill waybill);

    Waybill toWaybill(WaybillDto waybillDto);

    Collection<WaybillDto> toWaybillDtos(Collection<Waybill> waybills);

    Collection<Waybill> toWaybills(Collection<WaybillDto> waybillDtos);

}
