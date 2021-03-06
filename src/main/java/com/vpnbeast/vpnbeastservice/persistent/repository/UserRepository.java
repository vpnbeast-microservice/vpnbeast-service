package com.vpnbeast.vpnbeastservice.persistent.repository;

import com.vpnbeast.vpnbeastservice.persistent.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);

}