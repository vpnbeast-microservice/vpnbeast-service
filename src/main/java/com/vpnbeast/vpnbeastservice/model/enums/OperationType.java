package com.vpnbeast.vpnbeastservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum OperationType {

    // User related operations
    AUTHENTICATE_USER("authenticateUser"),
    INSERT_USER("insertUser"),
    UPDATE_USER("updateUser"),
    DELETE_USER("deleteUser"),
    ACTIVATE_USER("activateUser"),
    BLOCK_USER("blockUser"),
    GET_TOKEN("getToken"),
    VALIDATE_TOKEN("validateToken"),
    GET_ROLE("getRole"),
    GET_ALL_USERS("getAllUsers"),
    GET_USER("getUser"),
    GET_USER_BY_ID("getUserById"),
    LOGIN("login"),
    UPDATE_PASSWORD("updatePassword"),
    UPDATE_EMAIL("updateEmail"),
    RESET_PASSWORD("resetPassword"),
    VERIFY_USER("verifyUser"),
    RESEND_VERIFICATION_CODE("resendVerificationCode"),
    
    // Server related operations
    DELETE_SERVER("deleteServer"),
    DELETE_ROLE("deleteRole"),
    ACTIVATE_SERVER("activateServer"),
    BLOCK_SERVER("blockServer"),
    GET_SERVER("getServer"),
    GET_SERVER_BY_ID("getServerById"),
    UPDATE_SERVER("updateServer"),

    // Other stuff
    SEND_EMAIL("sendEmail");
    
    @Getter
    @Setter
    private String type;

}