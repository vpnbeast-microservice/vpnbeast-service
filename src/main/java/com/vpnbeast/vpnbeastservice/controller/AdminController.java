package com.vpnbeast.vpnbeastservice.controller;

import com.vpnbeast.vpnbeastservice.model.enums.ExceptionMessage;
import com.vpnbeast.vpnbeastservice.model.enums.OperationType;
import com.vpnbeast.vpnbeastservice.model.request.ServerBaseRequest;
import com.vpnbeast.vpnbeastservice.model.request.UserBaseRequest;
import com.vpnbeast.vpnbeastservice.model.response.SuccessResponse;
import com.vpnbeast.vpnbeastservice.model.response.UserResponse;
import com.vpnbeast.vpnbeastservice.persistent.entity.Server;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.service.ServerService;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import com.vpnbeast.vpnbeastservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
// @PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final ServerService serverService;
    private final ModelMapper modelMapper;
    private final ResponseService responseService;

    @GetMapping(value = "/users/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy) {
        List<User> userList = userService.getAll(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(responseService.buildUserResponseList(userList), new HttpHeaders(),
                HttpStatus.OK);
    }

    @PostMapping(value = "/users/")
    public ResponseEntity<UserResponse> getUser(@Valid @RequestBody UserBaseRequest request) {
        return new ResponseEntity<>(responseService.buildUserResponse(userService.get(modelMapper.map(request, User.class))),
                HttpStatus.FOUND);
    }

    @PostMapping(value = "/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(responseService.buildUserResponse(userService.getById(id)), HttpStatus.FOUND);
    }

    @PutMapping(value = "/users/activate")
    public ResponseEntity<SuccessResponse> activateUser(@Valid @RequestBody UserBaseRequest request) {
        final User entity = userService.get(modelMapper.map(request, User.class));
        entity.setEnabled(true);
        userService.update(entity);
        return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.ACTIVATE_USER.getType()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/users/block")
    public ResponseEntity<SuccessResponse> blockUser(@Valid @RequestBody UserBaseRequest request) {
        final User entity = userService.get(modelMapper.map(request, User.class));
        entity.setEnabled(false);
        userService.update(entity);
        return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.BLOCK_USER.getType()),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/delete")
    public ResponseEntity<SuccessResponse> deleteUser(@Valid @RequestBody UserBaseRequest request) {
        final User entity = userService.get(modelMapper.map(request, User.class));
        userService.delete(entity);
        return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.DELETE_USER.getType()),
                HttpStatus.OK);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long id) {
        return userService.deleteById(id) ? new ResponseEntity<>(responseService
                .buildSuccessResponse(OperationType.DELETE_USER.getType()), HttpStatus.OK) : new ResponseEntity<>(responseService
                .buildFailureResponse(OperationType.DELETE_USER.getType(), ExceptionMessage.UNKNOWN_ERROR_OCCURED.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(value = "/servers/activate")
    public ResponseEntity<SuccessResponse> activateServer(@Valid @RequestBody ServerBaseRequest request) {
        final Server entity = serverService.get(modelMapper.map(request, Server.class));
        entity.setEnabled(true);
        serverService.update(entity);
        return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.ACTIVATE_SERVER.getType()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/servers/block")
    public ResponseEntity<SuccessResponse> blockServer(@Valid @RequestBody ServerBaseRequest request) {
        final Server entity = serverService.get(modelMapper.map(request, Server.class));
        entity.setEnabled(false);
        serverService.update(entity);
        return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.BLOCK_SERVER.getType()),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "/servers/delete")
    public ResponseEntity<SuccessResponse> deleteUser(@Valid @RequestBody ServerBaseRequest request) {
        serverService.delete(modelMapper.map(request, Server.class));
        return new ResponseEntity<>(responseService.buildSuccessResponse(OperationType.DELETE_SERVER.getType()),
                HttpStatus.OK);
    }

    @DeleteMapping("/servers/delete/{id}")
    public ResponseEntity<Object> deleteServerById(@PathVariable Long id) {
        return serverService.deleteById(id) ? new ResponseEntity<>(responseService
                .buildSuccessResponse(OperationType.DELETE_SERVER.getType()), HttpStatus.OK) : new ResponseEntity<>(responseService
                .buildFailureResponse(OperationType.DELETE_SERVER.getType(), ExceptionMessage.UNKNOWN_ERROR_OCCURED.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}