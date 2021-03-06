package com.vpnbeast.vpnbeastservice.service.impl;

import com.vpnbeast.vpnbeastservice.client.EmailServiceClient;
import com.vpnbeast.vpnbeastservice.model.enums.EmailType;
import com.vpnbeast.vpnbeastservice.model.request.EmailRequest;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailServiceClient emailServiceClient;

    @Override
    public Boolean sendEmail(EmailType type, User user, Integer verificationCode) {
        final EmailRequest emailRequest = EmailRequest.builder()
                .emailType(type)
                .emailAddress(user.getEmail())
                .verificationCode(verificationCode)
                .build();
        return emailServiceClient.sendEmail(emailRequest).getStatus();
    }

}