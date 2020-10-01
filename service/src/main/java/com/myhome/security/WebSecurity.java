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
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
  private final Environment environment;
  private final ObjectMapper objectMapper;
  private final UserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailFetcher userDetailFetcher;
  private final AppJwtEncoderDecoder appJwtEncoderDecoder;

  @Override protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
    http.headers().frameOptions().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeRequests()
        .antMatchers(environment.getProperty("api.h2console.url.path"))
        .permitAll()
        .antMatchers(environment.getProperty("api.docs.url.path"))
        .permitAll()
        .antMatchers(environment.getProperty("api.status.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.POST, environment.getProperty("api.registration.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.POST, environment.getProperty("api.login.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilter(getAuthenticationFilter())
        .addFilter(new MyHomeAuthorizationFilter(authenticationManager(), environment,
            appJwtEncoderDecoder));
  }

  private Filter getAuthenticationFilter() throws Exception {
    MyHomeAuthenticationFilter authFilter =
        new MyHomeAuthenticationFilter(objectMapper, environment,
            authenticationManager(), userDetailFetcher, appJwtEncoderDecoder);
    authFilter.setFilterProcessesUrl(environment.getProperty("api.login.url.path"));
    return authFilter;
  }

  @Override protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}
