package com.vpnbeast.vpnbeastservice.service;

import com.vpnbeast.vpnbeastservice.model.enums.EmailType;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;

public interface EmailService {

    Boolean sendEmail(EmailType type, User user, Integer verificationCode);

}