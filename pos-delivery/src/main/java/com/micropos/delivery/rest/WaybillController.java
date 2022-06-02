package com.micropos.delivery.rest;

import com.micropos.api.WaybillsApi;
import com.micropos.delivery.mapper.WaybillMapper;
import com.micropos.delivery.repository.repository.WaybillRepository;
import com.micropos.dto.WaybillDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class WaybillController implements WaybillsApi {

    private final WaybillRepository waybillRepository;
    private final WaybillMapper waybillMapper;

    @Autowired
    public WaybillController(WaybillRepository waybillRepository, WaybillMapper waybillMapper) {
        this.waybillRepository = waybillRepository;
        this.waybillMapper = waybillMapper;
    }

    @Override
    public ResponseEntity<List<WaybillDto>> listWaybills() {
        var waybills = Streamable.of(waybillRepository.findAll()).toList();
        var waybillDtos = Streamable.of(waybillMapper.toWaybillDtos(waybills)).toList();

        return ResponseEntity.ok(waybillDtos);
    }
}
