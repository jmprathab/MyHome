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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * Custom {@link UsernamePasswordAuthenticationFilter} for catering to service need. Generates JWT
 * token as a response for Login request.
 */
public class MyHomeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final ObjectMapper objectMapper;
  private final Environment environment;
  private final AppUserDetailsService appUserDetailsService;

  public MyHomeAuthenticationFilter(ObjectMapper objectMapper,
      AppUserDetailsService appUserDetailsService,
      Environment environment,
      AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
    this.objectMapper = objectMapper;
    this.appUserDetailsService = appUserDetailsService;
    this.environment = environment;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
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
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {

    String username = ((User) authResult.getPrincipal()).getUsername();
    UserDto userDto = appUserDetailsService.getUserDetailsByUsername(username);

    final String base64EncodedSecretKey = environment.getProperty("token.secret");
    final SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64EncodedSecretKey));
    final long expirationTime =
        Long.parseLong(Objects.requireNonNull(environment.getProperty("token.expiration_time")));
    final Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

    String token = Jwts.builder()
        .setSubject(userDto.getUserId())
        .setExpiration(expirationDate)
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();

    response.addHeader("token", token);
    response.addHeader("userId", userDto.getUserId());
  }
}
