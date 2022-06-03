package com.by.gomel.gstu.viewModel;

import com.by.gomel.gstu.model.Detail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SailedDetailsViewModel {

    private Detail detail;
    private long detailsCount;

    public SailedDetailsViewModel(Detail detail, long detailsCount) {
        this.detail = detail;
        this.detailsCount = detailsCount;
    }
}
