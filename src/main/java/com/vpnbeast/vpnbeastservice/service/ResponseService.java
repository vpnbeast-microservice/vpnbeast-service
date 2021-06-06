package com.vpnbeast.vpnbeastservice.service;

import com.vpnbeast.vpnbeastservice.model.response.*;
import com.vpnbeast.vpnbeastservice.persistent.entity.Server;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;

import java.util.List;

public interface ResponseService {

    SuccessResponse buildSuccessResponse();
    FailureResponse buildFailureResponse(String errorMessage);
    UserResponse buildUserResponse(User user);
    List<UserResponse> buildUserResponseList(List<User> userList);
    ServerResponse buildServerResponse(Server server);
    JwtResponse buildJwtResponse(User user);
    List<ServerResponse> buildServerResponseList(List<Server> serverList);

}