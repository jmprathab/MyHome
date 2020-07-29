package com.myhome.security.jwt;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class AppJwt {
  private final String userId;
  private final LocalDateTime expiration;
}