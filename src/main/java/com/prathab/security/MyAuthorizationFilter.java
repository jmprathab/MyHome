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

package com.prathab.security;

import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class MyAuthorizationFilter extends BasicAuthenticationFilter {

  private final Environment environment;

  public MyAuthorizationFilter(
      AuthenticationManager authenticationManager,
      Environment environment) {
    super(authenticationManager);
    this.environment = environment;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    var authHeaderName = environment.getProperty("authorization.token.header.name");
    var authHeaderPrefix = environment.getProperty("authorization.token.header.prefix");

    var authHeader = request.getHeader(authHeaderName);
    if (authHeader == null || !authHeader.startsWith(authHeaderPrefix)) {
      chain.doFilter(request, response);
      return;
    }

    var authentication = getAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    var authHeader = request.getHeader(environment.getProperty("authorization.token.header.name"));
    if (authHeader == null) {
      return null;
    }

    var token =
        authHeader.replace(environment.getProperty("authorization.token.header.prefix"), "");
    var userId = Jwts.parser()
        .setSigningKey(environment.getProperty("token.secret"))
        .parseClaimsJws(token)
        .getBody()
        .getSubject();

    if (userId == null) {
      return null;
    }
    return new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
  }
}
