package com.vpnbeast.vpnbeastservice.persistent.service.impl;

import com.vpnbeast.vpnbeastservice.configuration.AuthenticationProperties;
import com.vpnbeast.vpnbeastservice.model.enums.EmailType;
import com.vpnbeast.vpnbeastservice.model.enums.ExceptionMessage;
import com.vpnbeast.vpnbeastservice.model.enums.OperationType;
import com.vpnbeast.vpnbeastservice.exception.*;
import com.vpnbeast.vpnbeastservice.model.enums.RoleType;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.repository.UserRepository;
import com.vpnbeast.vpnbeastservice.persistent.service.RoleService;
import com.vpnbeast.vpnbeastservice.persistent.service.UserService;
import com.vpnbeast.vpnbeastservice.service.EmailService;
import com.vpnbeast.vpnbeastservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final List<String> ALLOWED_SORT_BY_PARAMS = Stream.of("id", "userName", "email")
            .collect(Collectors.toList());
    private final UserRepository repository;
    private final RoleService roleService;
    private final EmailService emailService;
    private final AuthenticationProperties authenticationProperties;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        repository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public List<User> getAll(Integer pageNo, Integer pageSize, String sortBy) {
        if (!ALLOWED_SORT_BY_PARAMS.contains(sortBy))
            sortBy = "id";
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<User> pagedResult = repository.findAll(paging);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    @Override
    public User get(User request) {
        final Optional<User> user = repository.findByUserName(request.getUserName());
        return user.orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .tag(OperationType.GET_USER.getType())
                .status(false)
                .errorMessage(ExceptionMessage.NO_USER_FOUND.getMessage())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build()));
    }

    @Override
    public void insert(User entity) {
        try {
            if (isExists(entity))
                throw new ResourceAlreadyExistsException(ExceptionInfo.builder()
                        .tag(OperationType.INSERT_USER.getType())
                        .status(false)
                        .errorMessage(String.format(ExceptionMessage.ALREADY_USER.getMessage(), entity.getUserName()))
                        .httpCode(HttpStatus.BAD_REQUEST.value())
                        .timestamp(DateUtil.getCurrentLocalDateTime())
                        .build());
        } catch (ResourceNotFoundException exception) {
            log.info("User does not exists on database, inserting...");
        }

        try {
            if (isEmailTaken(entity))
                throw new ResourceAlreadyExistsException(ExceptionInfo.builder()
                        .tag(OperationType.INSERT_USER.getType())
                        .status(false)
                        .errorMessage(String.format(ExceptionMessage.EMAIL_ALREADY_TAKEN.getMessage(), entity.getEmail()))
                        .httpCode(HttpStatus.BAD_REQUEST.value())
                        .timestamp(DateUtil.getCurrentLocalDateTime())
                        .build());
        } catch (ResourceNotFoundException exception) {
            log.info("Email does not exists on database, inserting...");
        }

        final int verificationCode = generateVerificationCode();
        entity.setEncryptedPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setVerificationCode(verificationCode);
        entity.setVerificationCodeCreatedAt(LocalDateTime.now());
        entity.setVerificationCodeUsable(true);
        roleService.getRoleListByRoleTypes(RoleType.ROLE_USER).forEach(entity::addRole);
        // TODO: Return an error if something wrong happens while sending email
        emailService.sendEmail(EmailType.VALIDATE_EMAIL, entity, verificationCode);
        repository.save(entity);
    }

    @Override
    public void delete(User user) {
        if (isExists(user)) {
            repository.delete(user);
        }
    }

    private boolean isEmailTaken(User entity) {
        return Objects.nonNull(getByEmail(entity.getEmail()));
    }

    @Override
    public boolean deleteById(Long id) {
        return repository.findById(id).map(userEntity -> {
            log.info("Founded user by id {}", id);
            repository.deleteById(id);
            return true;
        }).orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .tag(OperationType.DELETE_USER.getType())
                .status(false)
                .errorMessage(ExceptionMessage.NO_USER_FOUND.getMessage())
                .httpCode(HttpStatus.BAD_REQUEST.value())
                .build()));
    }

    @Override
    public void update(User entity) {
        if (isExists(entity)) {
            repository.save(entity);
        }
    }

    @Override
    public boolean isExists(User entity) {
        return Objects.nonNull(get(entity));
    }

    @Override
    public User getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .tag(OperationType.GET_USER.getType())
                .status(false)
                .errorMessage(ExceptionMessage.NO_USER_FOUND.getMessage())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build()));
    }

    @Override
    public User getByUserName(String userName) {
        final Optional<User> userEntity = repository.findByUserName(userName);
        return userEntity.orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .tag(OperationType.GET_USER.getType())
                .status(false)
                .errorMessage(ExceptionMessage.NO_USER_FOUND.getMessage())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build()));
    }

    @Override
    public User getByEmail(String email) {
        final Optional<User> userEntity = repository.findByEmail(email);
        return userEntity.orElseThrow(() -> new ResourceNotFoundException(ExceptionInfo.builder()
                .tag(OperationType.GET_USER.getType())
                .status(false)
                .errorMessage(ExceptionMessage.NO_USER_FOUND.getMessage())
                .httpCode(HttpStatus.NOT_FOUND.value())
                .build()));
    }

    @Override
    public int generateVerificationCode() {
        return new Random().nextInt((999999-100000) + 1) + 100000;
    }

    private boolean isVerificationCodeUsable(User user, Integer verificationCode) {
        if (user.getVerificationCode().equals(verificationCode)) {
            if (user.getVerificationCodeCreatedAt().plusMinutes(authenticationProperties.getVerificationCodeValidInMinutes())
                    .isAfter(LocalDateTime.now())) {
                return true;
            } else {
                throw new VerificationCodeExpiredException(ExceptionInfo.builder()
                        .tag(OperationType.VERIFY_USER.getType())
                        .status(false)
                        .errorMessage(ExceptionMessage.VERIFICATION_CODE_EXPIRED.getMessage())
                        .httpCode(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build());
            }
        } else {
            throw new VerificationCodeNotValidException(ExceptionInfo.builder()
                    .tag(OperationType.VERIFY_USER.getType())
                    .status(false)
                    .errorMessage(ExceptionMessage.VERIFICATION_CODE_NOT_VALID.getMessage())
                    .httpCode(HttpStatus.BAD_REQUEST.value())
                    .timestamp(LocalDateTime.now())
                    .build());
        }
    }

    @Override
    public boolean verify(String email, Integer verificationCode) {
        final User user = getByEmail(email);
        if (isVerificationCodeUsable(user, verificationCode)) {
            user.setEmailVerified(true);
            user.setVerificationCodeUsable(false);
            user.setVerificationCodeVerifiedAt(LocalDateTime.now());
            repository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void resendVerificationCode(String email, EmailType emailType) {
        final User user = getByEmail(email);
        final int verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeUsable(true);
        user.setEmailVerified(false);
        user.setVerificationCodeVerifiedAt(null);
        user.setVerificationCodeCreatedAt(LocalDateTime.now());
        repository.save(user);
        // TODO: Return an error if something wrong happens while sending email
        emailService.sendEmail(emailType, user, verificationCode);
    }

    @Override
    public boolean resetPassword(String email, Integer verificationCode, String password) {
        if (verify(email, verificationCode)) {
            final User user = getByEmail(email);
            user.setEncryptedPassword(passwordEncoder.encode(password));
            repository.save(user);
            return true;
        }
        return false;
    }

}