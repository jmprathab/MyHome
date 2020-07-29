/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.security.jwt.impl;

import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Concrete implementation of {@link AppJwtEncoderDecoder}. Use this only in testing.
 */
@Profile("test")
@Component
public class NoSecretJwtEncoderDecoder implements AppJwtEncoderDecoder {
  private static final String SEPARATOR = "\\+";

  @Override public AppJwt decode(String encodedJwt, String secret) {
    String[] strings = encodedJwt.split(SEPARATOR);
    return AppJwt.builder().userId(strings[0]).expiration(LocalDateTime.parse(strings[1])).build();
  }

  @Override public String encode(AppJwt jwt, String secret) {
    return jwt.getUserId() + SEPARATOR + jwt.getExpiration();
  }
}
