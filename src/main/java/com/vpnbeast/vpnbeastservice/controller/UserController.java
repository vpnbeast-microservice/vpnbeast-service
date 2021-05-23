package com.vpnbeast.vpnbeastservice.controller;

import com.vpnbeast.vpnbeastservice.model.enums.ExceptionMessage;
import com.vpnbeast.vpnbeastservice.model.enums.OperationType;
import com.vpnbeast.vpnbeastservice.model.request.*;
import com.vpnbeast.vpnbeastservice.model.response.JwtResponse;
import com.vpnbeast.vpnbeastservice.model.response.SuccessResponse;
import com.vpnbeast.vpnbeastservice.model.response.UserResponse;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import com.vpnbeast.vpnbeastservice.service.JwtTokenService;
import com.vpnbeast.vpnbeastservice.service.ResponseService;
import com.vpnbeast.vpnbeastservice.persistent.service.impl.JwtUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;
    private final JwtTokenService jwtTokenService;
    private final JwtUserDetailsServiceImpl userDetailsService;

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<JwtResponse> refresh(HttpServletRequest req) {
        final String userName = req.getRemoteUser();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return new ResponseEntity<>(jwtTokenService.generateTokenResponse(userDetails), HttpStatus.OK);
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserResponse> whoami(HttpServletRequest req) {
        final String userName = req.getRemoteUser();
        return new ResponseEntity<>(responseService.buildUserResponse(userService.getByUserName(userName)),
                HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<SuccessResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        final User user = modelMapper.map(request, User.class);
        userService.insert(user);
        return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.INSERT_USER.getType()),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<Object> verifyUser(@Valid @RequestBody UserVerifyRequest request) {
        // TODO: fix
        if (userService.verify(request.getEmail(), request.getVerificationCode()))
            return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.VERIFY_USER.getType()),
                    HttpStatus.OK);
        else
            return new ResponseEntity<>(responseService.buildFailureResponse(OperationType.VERIFY_USER.getType(),
                    ExceptionMessage.USER_VERIFICATION_FAILED.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/resend-verification-code")
    public ResponseEntity<Object> resendVerificationCode(@Valid @RequestBody UserResendVerificationCodeRequest request) {
        userService.resendVerificationCode(request.getEmail(), request.getEmailType());
        return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.RESEND_VERIFICATION_CODE.getType()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody UserResetPasswordRequest request) {
        // TODO: fix
        if (userService.resetPassword(request.getEmail(), request.getVerificationCode(), request.getPassword()))
            return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.RESET_PASSWORD.getType()),
                    HttpStatus.OK);
        else
            return new ResponseEntity<>(responseService.buildFailureResponse(OperationType.RESET_PASSWORD.getType(),
                    ExceptionMessage.USER_VERIFICATION_FAILED.getMessage()), HttpStatus.BAD_REQUEST);
    }

}