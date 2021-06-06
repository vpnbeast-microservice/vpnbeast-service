package com.vpnbeast.vpnbeastservice.persistent.service.impl;

import com.vpnbeast.vpnbeastservice.exception.ExceptionInfo;
import com.vpnbeast.vpnbeastservice.exception.ResourceAlreadyExistsException;
import com.vpnbeast.vpnbeastservice.exception.ResourceNotFoundException;
import com.vpnbeast.vpnbeastservice.model.enums.ExceptionMessage;
import com.vpnbeast.vpnbeastservice.model.enums.RoleType;
import com.vpnbeast.vpnbeastservice.persistent.entity.Role;
import com.vpnbeast.vpnbeastservice.persistent.repository.RoleRepository;
import com.vpnbeast.vpnbeastservice.persistent.service.RoleService;
import com.vpnbeast.vpnbeastservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final List<String> ALLOWED_SORT_BY_PARAMS = Stream.of("id", "name").collect(Collectors.toList());
    private final RoleRepository repository;

    @Override
    public List<Role> getAll() {
        List<Role> roles = new ArrayList<>();
        repository.findAll().forEach(roles::add);
        return roles;
    }

    @Override
    public List<Role> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        if (!ALLOWED_SORT_BY_PARAMS.contains(sortBy))
            sortBy = "id";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<Role> pagedResult = repository.findAll(paging);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    @Override
    public Role get(Role entity) {
        final Optional<Role> role = repository.findByName(entity.getName());
        return role.orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .status(false)
                .errorMessage(ExceptionMessage.NO_ROLE_FOUND.getMessage())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build()));
    }

    @Override
    public void delete(Role role) {
        if (isExists(role)) {
            repository.delete(role);
        }
    }

    @Override
    public Role getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .status(false)
                .errorMessage(ExceptionMessage.NO_ROLE_FOUND.getMessage())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build()));
    }

    @Override
    public void update(Role entity) {
        if (isExists(entity)) {
            repository.save(entity);
        }
    }

    @Override
    public boolean isExists(Role entity) {
        return Objects.nonNull(get(entity));
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.findById(id).map(role -> {
            repository.deleteById(id);
            return true;
        }).orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .status(false)
                .errorMessage(ExceptionMessage.NO_ROLE_FOUND.getMessage())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build()));
    }

    @Override
    public void insert(Role entity) {
        try {
            if (isExists(entity))
                throw new ResourceAlreadyExistsException(ExceptionInfo.builder()
                        .status(false)
                        .errorMessage(ExceptionMessage.ROLE_ALREADY_EXISTS.getMessage())
                        .httpCode(HttpStatus.BAD_REQUEST.value())
                        .timestamp(DateUtil.getCurrentLocalDateTime())
                        .build());
        } catch (ResourceNotFoundException exception) {
            log.info("Resource does not exists on database, inserting...");
        }
        repository.save(entity);
    }

    @Override
    public List<Role> getRoleListByRoleTypes(RoleType ... type) {
        List<Role> roleList = new ArrayList<>();
        for (RoleType role : type) {
            roleList.add(repository.findByName(role).orElse(null));
        }
        return roleList;
    }

}