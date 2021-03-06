package com.vpnbeast.vpnbeastservice.model.request;

import lombok.*;
import javax.validation.constraints.*;

@Getter
@Setter
public class UserRegisterRequest extends UserBaseRequest {
    
    @NotNull(message = "Password must not be null!")
    @Size(min = 5, max = 200, message = "Password must be between 5 and 200 characters!")
    private String password;

    @NotNull(message = "Email address must not be null!")
    @Email(message = "Not a valid email format!")
    private String email;

}