package com.vpnbeast.vpnbeastservice.model.request;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserUpdateEmailRequest extends UserBaseRequest {

    @NotNull(message = "Email address must not be null!")
    @Email(message = "Not a valid email format!")
    private String email;
    
}