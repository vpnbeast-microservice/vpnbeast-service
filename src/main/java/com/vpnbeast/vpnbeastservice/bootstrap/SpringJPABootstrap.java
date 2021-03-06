package com.vpnbeast.vpnbeastservice.bootstrap;

import com.vpnbeast.vpnbeastservice.model.enums.RoleType;
import com.vpnbeast.vpnbeastservice.persistent.entity.Role;
import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import com.vpnbeast.vpnbeastservice.persistent.repository.RoleRepository;
import com.vpnbeast.vpnbeastservice.persistent.repository.UserRepository;
import com.vpnbeast.vpnbeastservice.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Profile("local")
@RequiredArgsConstructor
public class SpringJPABootstrap implements ApplicationListener<ApplicationReadyEvent> {

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String REGULAR_USER = "bilal";
    private static final String REGULAR_PASS = "bilal123";
    private static final String REGULAR_EMAIL = "bilal@example.com";
    private static final String REGULAR_ROLE = "ROLE_USER";

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private EncryptionService encryptionService;

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent applicationReadyEvent) {
        loadUsers();
        loadRoles();
        assignUsersToDefaultRole();
        assignUsersToAdminRole();
    }

    private void loadUsers() {
        User admin = User.builder()
                .uuid(UUID.randomUUID().toString())
                .userName(ADMIN_USER)
                .password(ADMIN_PASS)
                .encryptedPassword(encryptionService.encrypt(ADMIN_PASS))
                .email(ADMIN_EMAIL)
                .failedLoginAttempts(0)
                .enabled(true)
                .roles(new ArrayList<>())
                .lastLogin(null)
                .build();
        userRepository.save(admin);

        User user = User.builder()
                .uuid(UUID.randomUUID().toString())
                .userName(REGULAR_USER)
                .password(REGULAR_PASS)
                .encryptedPassword(encryptionService.encrypt(REGULAR_PASS))
                .email(REGULAR_EMAIL)
                .failedLoginAttempts(0)
                .enabled(true)
                .roles(new ArrayList<>())
                .lastLogin(null)
                .build();
        userRepository.save(user);
    }

    private void loadRoles() {
        Role userRole = Role.builder()
                .name(RoleType.ROLE_USER)
                .build();
        roleRepository.save(userRole);

        Role adminRole = Role.builder()
                .name(RoleType.ROLE_ADMIN)
                .build();
        roleRepository.save(adminRole);
    }

    private void assignUsersToAdminRole() {
        List<Role> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);

        roles.forEach(role -> {
            if (role.getName().name().equalsIgnoreCase(ADMIN_ROLE)) {
                users.forEach(user -> {
                    if (user.getUserName().equals(ADMIN_USER)) {
                        user.addRole(role);
                        userRepository.save(user);
                    }
                });
            }
        });
    }

    private void assignUsersToDefaultRole() {
        List<Role> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);

        roles.forEach(role -> {
            if (role.getName().name().equalsIgnoreCase(REGULAR_ROLE)) {
                users.forEach(user -> {
                    if (user.getUserName().equals(REGULAR_USER)) {
                        user.addRole(role);
                        userRepository.save(user);
                    }
                });
            }
        });
    }

}