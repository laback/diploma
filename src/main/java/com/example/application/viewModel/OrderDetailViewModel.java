package com.example.application.viewModel;

import com.example.application.model.Detail;
import lombok.Getter;

@Getter
public class OrderDetailViewModel {

    private final Detail detail;

    private final int detailsCount;

    private final float detailsCost;

    public OrderDetailViewModel(Detail detail, int detailsCount) {
        this.detail = detail;
        this.detailsCount = detailsCount;
        this.detailsCost = detail.getDetailCost() * detailsCount;
    }
}
