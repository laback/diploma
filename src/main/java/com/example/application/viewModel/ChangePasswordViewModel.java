package com.example.application.viewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordViewModel {

    private String oldPassword;

    private String newPassword;
    private String confirmPassword;
}
