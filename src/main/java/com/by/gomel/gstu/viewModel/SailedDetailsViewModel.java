package com.by.gomel.gstu.viewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SailedDetailsViewModel {

    String detailName;
    String detailDescription;
    int detailsCount;

    public SailedDetailsViewModel(String detailName, String detailDescription, int detailsCount) {
        this.detailName = detailName;
        this.detailDescription = detailDescription;
        this.detailsCount = detailsCount;
    }
}
