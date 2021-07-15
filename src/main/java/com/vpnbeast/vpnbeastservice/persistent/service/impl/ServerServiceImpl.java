package com.vpnbeast.vpnbeastservice.persistent.service.impl;

import com.vpnbeast.vpnbeastservice.model.enums.ExceptionMessage;
import com.vpnbeast.vpnbeastservice.exception.ExceptionInfo;
import com.vpnbeast.vpnbeastservice.exception.ResourceNotFoundException;
import com.vpnbeast.vpnbeastservice.persistent.entity.Server;
import com.vpnbeast.vpnbeastservice.persistent.repository.ServerRepository;
import com.vpnbeast.vpnbeastservice.persistent.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private static final List<String> ALLOWED_SORT_BY_PARAMS = Stream.of("id", "score", "ping", "speed", "numVpnSessions",
            "uptime", "totalUsers", "totalTraffic").collect(Collectors.toList());
    private final ServerRepository repository;

    @Override
    public List<Server> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        if (!ALLOWED_SORT_BY_PARAMS.contains(sortBy))
            sortBy = "id";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Server> pagedResult = repository.findAll(paging);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    @Override
    public Server get(Server entity) {
        final Optional<Server> serverEntity = repository.findByIpAndPortAndProto(entity.getIp(),
                entity.getPort(), entity.getProto());
        return serverEntity.orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .status(false)
                .errorMessage(ExceptionMessage.NO_SERVER_FOUND.getMessage())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build()));
    }

    @Override
    public Server getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .status(false)
                .errorMessage(ExceptionMessage.NO_SERVER_FOUND.getMessage())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build()));
    }

    @Override
    public void update(Server entity) {
        if (isExists(entity)) {
            repository.save(entity);
        }
    }

    @Override
    public boolean isExists(Server entity) {
        return Objects.nonNull(get(entity));
    }

    @Override
    public void delete(Server entity) {
        if (isExists(entity))
            repository.delete(entity);
    }

    @Override
    public boolean deleteById(Long serverId) {
        return repository.findById(serverId).map(server -> {
            log.info("Founded server by id {}", serverId);
            repository.deleteById(serverId);
            return true;
        }).orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .status(false)
                .errorMessage(ExceptionMessage.NO_SERVER_FOUND.getMessage())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build()));
    }
    
}