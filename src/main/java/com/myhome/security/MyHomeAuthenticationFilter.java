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

package com.myhome.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.request.LoginUserRequest;
import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Custom {@link UsernamePasswordAuthenticationFilter} for catering to service need. Generates JWT
 * token as a response for Login request.
 */
public class MyHomeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final ObjectMapper objectMapper;
  private final Environment environment;
  private final UserDetailFetcher userDetailFetcher;
  private final AppJwtEncoderDecoder appJwtEncoderDecoder;

  public MyHomeAuthenticationFilter(ObjectMapper objectMapper,
      Environment environment,
      AuthenticationManager authenticationManager,
      UserDetailFetcher userDetailFetcher,
      AppJwtEncoderDecoder appJwtEncoderDecoder) {
    this.appJwtEncoderDecoder = appJwtEncoderDecoder;
    super.setAuthenticationManager(authenticationManager);
    this.userDetailFetcher = userDetailFetcher;
    this.objectMapper = objectMapper;
    this.environment = environment;
  }

  @Override public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    try {
      LoginUserRequest loginUserRequest =
          objectMapper.readValue(request.getInputStream(), LoginUserRequest.class);
      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(),
              loginUserRequest.getPassword(), Collections.emptyList()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    String username = ((User) authResult.getPrincipal()).getUsername();
    UserDto userDto = userDetailFetcher.getUserDetailsByUsername(username);

    LocalDateTime expiration = new Date(System.currentTimeMillis() + Long.parseLong(
        environment.getProperty("token.expiration_time"))).toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
    AppJwt jwt = AppJwt.builder().userId(userDto.getUserId()).expiration(expiration).build();
    String token = appJwtEncoderDecoder.encode(jwt, environment.getProperty("token.secret"));
    response.addHeader("token", token);
    response.addHeader("userId", userDto.getUserId());
  }
}
