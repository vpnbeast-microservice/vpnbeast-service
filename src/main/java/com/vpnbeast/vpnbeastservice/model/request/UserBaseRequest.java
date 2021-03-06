package com.vpnbeast.vpnbeastservice.model.request;

import lombok.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBaseRequest {

    private Long id;
    
    @NotNull(message = "User name must not be null!")
    @Size(min = 3, max = 40, message = "User name must be between 3 and 40 characters!")
    private String userName;
    
}