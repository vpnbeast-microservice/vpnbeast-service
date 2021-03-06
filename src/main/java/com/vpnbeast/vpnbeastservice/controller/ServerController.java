package com.vpnbeast.vpnbeastservice.controller;

import com.vpnbeast.vpnbeastservice.model.request.ServerBaseRequest;
import com.vpnbeast.vpnbeastservice.model.response.ServerResponse;
import com.vpnbeast.vpnbeastservice.persistent.entity.Server;
import com.vpnbeast.vpnbeastservice.persistent.service.ServerService;
import com.vpnbeast.vpnbeastservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/servers")
public class ServerController {

    private final ServerService serverService;
    private final ModelMapper modelMapper;
    private final ResponseService responseService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<ServerResponse>> getAllServers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                              @RequestParam(defaultValue = "10") Integer pageSize,
                                                              @RequestParam(defaultValue = "id") String sortBy) {
        List<Server> serverList = serverService.getAll(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(responseService.buildServerResponseList(serverList), new HttpHeaders(),
                HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<ServerResponse> getServer(@Valid @RequestBody ServerBaseRequest request) {
        return new ResponseEntity<>(responseService.buildServerResponse(serverService.get(modelMapper.map(request, Server.class))),
                HttpStatus.FOUND);
    }

}