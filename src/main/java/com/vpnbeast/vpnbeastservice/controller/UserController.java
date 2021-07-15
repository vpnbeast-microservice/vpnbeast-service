package com.vpnbeast.vpnbeastservice.controller;

import com.vpnbeast.vpnbeastservice.model.enums.ExceptionMessage;
import com.vpnbeast.vpnbeastservice.model.request.*;
import com.vpnbeast.vpnbeastservice.model.response.SuccessResponse;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import com.vpnbeast.vpnbeastservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/register")
    public ResponseEntity<SuccessResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        final User user = modelMapper.map(request, User.class);
        userService.insert(user);
        return new ResponseEntity<>(responseService.buildSuccessResponse(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<Object> verifyUser(@Valid @RequestBody UserVerifyRequest request) {
        // TODO: fix
        if (userService.verify(request.getEmail(), request.getVerificationCode()))
            return new ResponseEntity<>(responseService.buildSuccessResponse(), HttpStatus.OK);
        else
            return new ResponseEntity<>(responseService.buildFailureResponse(ExceptionMessage.USER_VERIFICATION_FAILED
                    .getMessage()), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/resend-verification-code")
    public ResponseEntity<Object> resendVerificationCode(@Valid @RequestBody UserResendVerificationCodeRequest request) {
        userService.resendVerificationCode(request.getEmail(), request.getEmailType());
        return new ResponseEntity<>(responseService.buildSuccessResponse(), HttpStatus.OK);
    }

    @PutMapping(value = "/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody UserResetPasswordRequest request) {
        // TODO: fix
        if (userService.resetPassword(request.getEmail(), request.getVerificationCode(), request.getPassword()))
            return new ResponseEntity<>(responseService.buildSuccessResponse(), HttpStatus.OK);
        else
            return new ResponseEntity<>(responseService.buildFailureResponse(ExceptionMessage.USER_VERIFICATION_FAILED
                    .getMessage()), HttpStatus.BAD_REQUEST);
    }

}