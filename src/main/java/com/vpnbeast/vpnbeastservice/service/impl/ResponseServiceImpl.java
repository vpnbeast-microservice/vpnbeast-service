package com.vpnbeast.vpnbeastservice.service.impl;

import com.vpnbeast.vpnbeastservice.model.response.*;
import com.vpnbeast.vpnbeastservice.persistent.entity.Server;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.service.ResponseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {

    @Override
    public SuccessResponse buildSuccessResponse() {
        return SuccessResponse.builder()
                .status(true)
                .build();
    }

    @Override
    public FailureResponse buildFailureResponse(String errorMessage) {
        return FailureResponse.builder()
                .status(false)
                .errorMessage(errorMessage)
                .build();
    }

    @Override
    public UserResponse buildUserResponse(User user) {
        return UserResponse.userResponseBuilder()
                .user(user)
                .build();
    }

    @Override
    public List<UserResponse> buildUserResponseList(List<User> userList) {
        List<UserResponse> responseList = new ArrayList<>();
        userList.forEach(user ->
            responseList.add(buildUserResponse(user))
        );
        return responseList;
    }

    @Override
    public ServerResponse buildServerResponse(Server server) {
        return ServerResponse.serverResponseBuilder()
                .server(server)
                .build();
    }

    @Override
    public JwtResponse buildJwtResponse(User user) {
        return JwtResponse.jwtResponseBuilder()
                .user(user)
                .build();
    }

    @Override
    public List<ServerResponse> buildServerResponseList(List<Server> serverList) {
        List<ServerResponse> responseList = new ArrayList<>();
        serverList.forEach(server ->
                responseList.add(buildServerResponse(server))
        );
        return responseList;
    }

}