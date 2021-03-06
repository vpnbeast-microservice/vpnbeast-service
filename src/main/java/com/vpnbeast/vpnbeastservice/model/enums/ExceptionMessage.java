package com.vpnbeast.vpnbeastservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum ExceptionMessage {

    USER_PASS_INCORRECT("Password is incorrect!"),
    USER_DISABLED("User is currently blocked!"),
    JWT_EXPIRED("Given Jwt token already expired!"),
    NO_USER_FOUND("User not found!"),
    USER_VERIFICATION_FAILED("User verification failed, check the verification code!"),
    VERIFICATION_CODE_EXPIRED("Verification code already expired, please resend verification code!"),
    VERIFICATION_CODE_NOT_VALID("Verification code is not valid, please try with a valid code!"),
    NO_SERVER_FOUND("Server not found!"),
    NO_ROLE_FOUND("Role not found!"),
    ALREADY_USER("User %s already exists!"),
    EMAIL_ALREADY_TAKEN("Email address %s already taken by another user!"),
    ROLE_ALREADY_EXISTS("Role already exists!"),
    UNKNOWN_ERROR_OCCURED("Unknown error occured!");

    @Getter
    @Setter
    private String message;

}