package com.by.gomel.gstu.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ordersdetails")
@Data
public class OrdersDetail extends BaseEntity{

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(targetEntity = Detail.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "detail_id")
    private Detail detail;

    private int count;

    public OrdersDetail(Order order, Detail detail, int count) {
        this.order = order;
        this.detail = detail;
        this.count = count;
    }

    public OrdersDetail() {
    }
}
