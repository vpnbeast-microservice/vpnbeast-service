package com.vpnbeast.vpnbeastservice.model.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserVerifyRequest {

    @NotNull(message = "Email address must not be null!")
    @Email(message = "Not a valid email format!")
    private String email;

    @NotNull(message = "Verification code must not be null!")
    @Min(value = 100000, message = "Verification code must be minimum 100000!")
    @Max(value = 999999, message = "Verification code must be maximum 999999!")
    private Integer verificationCode;

}