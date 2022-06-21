package com.by.gomel.gstu.viewModel;

import com.by.gomel.gstu.model.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderViewModel {
    private Order order;
    private String details;

    public OrderViewModel(Order order, String details) {
        this.order = order;
        this.details = details;
    }
}
