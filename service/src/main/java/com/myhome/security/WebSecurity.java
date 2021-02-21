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

import com.myhome.security.filters.CommunityAuthorizationFilter;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import com.myhome.services.CommunityService;
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

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
  private final Environment environment;
  private final UserDetailsService userDetailsService;
  private final CommunityService communityService;
  private final PasswordEncoder passwordEncoder;
  private final AppJwtEncoderDecoder appJwtEncoderDecoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
    http.headers().frameOptions().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterAfter(getCommunityFilter(), MyHomeAuthorizationFilter.class);

    http.authorizeRequests()
        .antMatchers(environment.getProperty("api.public.h2console.url.path"))
        .permitAll()
        .antMatchers(environment.getProperty("api.public.actuator.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.POST, environment.getProperty("api.public.registration.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.POST, environment.getProperty("api.public.login.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.OPTIONS, environment.getProperty("api.public.cors.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.GET, environment.getProperty("api.public.confirm-email.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.GET, environment.getProperty("api.public.resend-confirmation-email.url.path"))
        .permitAll()
        .antMatchers(HttpMethod.POST, environment.getProperty("api.public.confirm-email.url.path"))
        .permitAll()
        .antMatchers("/swagger/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilter(new MyHomeAuthorizationFilter(authenticationManager(), environment,
            appJwtEncoderDecoder))
        .addFilterAfter(getCommunityFilter(), MyHomeAuthorizationFilter.class);
  }

  private Filter getCommunityFilter() throws Exception {
    return new CommunityAuthorizationFilter(authenticationManager(), communityService);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}
