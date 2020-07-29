package com.myhome.security.jwt;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AppJwtParamTest {

  @Test
  void testParamCreationBuilder() {
    AppJwt param = AppJwt.builder().userId("test-user-id").expiration(LocalDateTime.now()).build();
    System.out.println(param);
  }
}