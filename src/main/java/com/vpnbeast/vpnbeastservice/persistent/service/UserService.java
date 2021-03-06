package com.vpnbeast.vpnbeastservice.persistent.service;

import com.vpnbeast.vpnbeastservice.model.enums.EmailType;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;

public interface UserService extends CRUDService<User> {

    void insert(User entity);
    User getByUserName(String userName);
    User getByEmail(String email);
    int generateVerificationCode();
    boolean verify(String email, Integer verificationCode);
    void resendVerificationCode(String email, EmailType emailType);
    boolean resetPassword(String email, Integer verificationCode, String password);

}