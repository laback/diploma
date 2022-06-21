package com.by.gomel.gstu.viewModel;

import com.by.gomel.gstu.model.Detail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailViewModel {

    private Detail detail;
    private String stringAttributes;

    public DetailViewModel(Detail detail, String stringAttributes) {
        this.detail = detail;
        this.stringAttributes = stringAttributes;
    }
}
