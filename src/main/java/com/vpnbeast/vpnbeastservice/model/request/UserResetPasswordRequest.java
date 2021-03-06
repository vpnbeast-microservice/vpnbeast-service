package com.vpnbeast.vpnbeastservice.model.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
public class UserResetPasswordRequest {

    @NotNull(message = "Email address must not be null!")
    @Email(message = "Not a valid email format!")
    private String email;

    @NotNull(message = "Password must not be null!")
    @Size(min = 5, max = 200, message = "Password must be between 5 and 200 characters!")
    private String password;

    @NotNull(message = "Verification code must not be null!")
    @Min(value = 100000, message = "Verification code must be minimum 100000!")
    @Max(value = 999999, message = "Verification code must be maximum 999999!")
    private Integer verificationCode;

}