package com.vpnbeast.vpnbeastservice.persistent.repository;

import com.vpnbeast.vpnbeastservice.model.enums.RoleType;
import com.vpnbeast.vpnbeastservice.persistent.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);

}