package com.example.application.viewModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordViewModel {

    private String newPassword;
    private String confirmPassword;
    private String token;

}
