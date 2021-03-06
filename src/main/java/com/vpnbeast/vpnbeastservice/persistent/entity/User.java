package com.vpnbeast.vpnbeastservice.persistent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true)
    private String userName;

    @Column(columnDefinition = "TEXT")
    private String uuid = UUID.randomUUID().toString();

    @Transient
    @JsonIgnore
    private String password;

    @JsonIgnore
    private String encryptedPassword;

    @Column(columnDefinition = "TEXT")
    private String accessToken;

    private LocalDateTime accessTokenExpiresAt;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    private LocalDateTime refreshTokenExpiresAt;

    @Column(unique = true)
    private String email;

    private LocalDateTime lastLogin;
    private Boolean enabled = true;
    private Boolean emailVerified = false;
    private Integer failedLoginAttempts = 0;

    private Integer verificationCode;
    private Boolean verificationCodeUsable = false;
    private LocalDateTime verificationCodeCreatedAt;
    private LocalDateTime verificationCodeVerifiedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    // ~ defaults to @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "user_id"),
    //     inverseJoinColumns = @joinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    public void addRole(Role role){
        if (!this.roles.contains(role))
            this.roles.add(role);
    }

    public void removeRole(Role role){
        this.roles.remove(role);
    }

}