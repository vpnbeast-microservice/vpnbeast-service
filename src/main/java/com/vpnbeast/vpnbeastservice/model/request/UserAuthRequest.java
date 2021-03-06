package com.vpnbeast.vpnbeastservice.model.request;

import lombok.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserAuthRequest extends UserBaseRequest {

    @NotNull(message = "Password must not be null!")
    @Size(min = 5, max = 200, message = "Password must be between 5 and 200 characters!")
    private String password;

}