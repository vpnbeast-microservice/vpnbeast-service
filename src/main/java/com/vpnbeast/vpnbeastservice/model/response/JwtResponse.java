package com.vpnbeast.vpnbeastservice.model.response;

import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class JwtResponse extends BaseEntityResponse {

    private String userName;
    private String email;
    private LocalDateTime lastLogin;
    private Boolean enabled;
    private Boolean emailVerified;
    private String tag;
    private String accessToken;
    private LocalDateTime accessTokenExpiresAt;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiresAt;

    @Builder(builderMethodName = "jwtResponseBuilder")
    public JwtResponse(User user) {
        super(user.getUuid(), user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getVersion());
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.lastLogin = user.getLastLogin();
        this.enabled = user.getEnabled();
        this.emailVerified = user.getEmailVerified();
        this.tag = "getToken";
        this.accessToken = user.getAccessToken();
        this.accessTokenExpiresAt = user.getAccessTokenExpiresAt();
        this.refreshToken = user.getRefreshToken();
        this.refreshTokenExpiresAt = user.getRefreshTokenExpiresAt();
    }

}