package com.vpnbeast.vpnbeastservice.model.request;

import com.vpnbeast.vpnbeastservice.model.enums.EmailType;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserResendVerificationCodeRequest {

    @NotNull(message = "Email address must not be null!")
    @Email(message = "Not a valid email format!")
    private String email;

    @NotNull(message = "Email type must not be null!")
    private EmailType emailType;

}