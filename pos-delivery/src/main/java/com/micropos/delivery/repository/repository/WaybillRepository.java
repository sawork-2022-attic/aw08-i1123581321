package com.micropos.delivery.repository.repository;

import com.micropos.delivery.model.Waybill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaybillRepository extends CrudRepository<Waybill, Integer> {
}
