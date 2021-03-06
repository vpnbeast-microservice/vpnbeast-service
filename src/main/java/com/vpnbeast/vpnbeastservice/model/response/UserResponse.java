package com.vpnbeast.vpnbeastservice.model.response;

import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse extends BaseEntityResponse {

    private String userName;
    private String email;
    private LocalDateTime lastLogin;
    private Boolean enabled;
    private Boolean emailVerified;
    private Integer verificationCode;
    private Boolean verificationCodeUsable;
    private LocalDateTime verificationCodeCreatedAt;
    private LocalDateTime verificationCodeVerifiedAt;

    @Builder(builderMethodName = "userResponseBuilder")
    public UserResponse(User user) {
        super(user.getUuid(), user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getVersion());
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.lastLogin = user.getLastLogin();
        this.enabled = user.getEnabled();
        this.emailVerified = user.getEmailVerified();
        this.verificationCode = user.getVerificationCode();
        this.verificationCodeUsable = user.getVerificationCodeUsable();
        this.verificationCodeCreatedAt = user.getVerificationCodeCreatedAt();
        this.verificationCodeVerifiedAt = user.getVerificationCodeVerifiedAt();
    }

}