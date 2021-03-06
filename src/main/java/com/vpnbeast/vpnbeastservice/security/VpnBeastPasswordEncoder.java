package com.vpnbeast.vpnbeastservice.security;

import com.vpnbeast.vpnbeastservice.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class VpnBeastPasswordEncoder implements PasswordEncoder {

    private final EncryptionService encryptionService;

    @Override
    public String encode(CharSequence charSequence) {
        return encryptionService.encrypt(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encryptionService.check(charSequence.toString(), s);
    }

}