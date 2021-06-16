package com.vpnbeast.vpnbeastservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// @ActiveProfiles("unit-test")
@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
public class VpnbeastServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
