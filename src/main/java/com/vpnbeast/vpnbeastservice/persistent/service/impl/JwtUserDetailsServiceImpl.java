package com.vpnbeast.vpnbeastservice.persistent.service.impl;

import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        final User entity = userService.getByUserName(userName);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        entity.getRoles().forEach(role ->
            authorities.add(new SimpleGrantedAuthority(role.getName().name()))
        );
        return org.springframework.security.core.userdetails.User
            .withUsername(entity.getUserName())
            .password(entity.getEncryptedPassword())
            .authorities(authorities)
            .accountExpired(false)
            .disabled(false)
            .credentialsExpired(false)
            .accountLocked(false)
            .build();
    }

    public void authenticate(AuthenticationManager authenticationManager, String userName, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
    }

}