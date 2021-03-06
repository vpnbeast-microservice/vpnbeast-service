package com.vpnbeast.vpnbeastservice.persistent.repository;

import com.vpnbeast.vpnbeastservice.persistent.entity.Server;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface ServerRepository extends PagingAndSortingRepository<Server, Long> {

    @Transactional
    void deleteServerByIpAndPortAndProto(@Param("ip") String ip, @Param("port") Integer port,
                                         @Param("proto") String proto);

    Optional<Server> findByIpAndPortAndProto(@Param("ip") String ip, @Param("port") Integer port,
                                             @Param("proto") String proto);

}