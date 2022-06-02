package com.micropos.delivery.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "waybills")
public class Waybill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "order_id")
    private Integer orderId;

    public Waybill(Integer orderId) {
        this.orderId = orderId;
    }

    public Waybill() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
