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

package com.myhome.controllers;

import com.myhome.domain.AuthenticationData;
import com.myhome.model.LoginRequest;
import com.myhome.security.jwt.AppJwt;
import com.myhome.security.jwt.AppJwtEncoderDecoder;
import com.myhome.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class AuthenticationControllerTest {

    private static final String TEST_ID = "1";
    private static final String TEST_EMAIL = "email@mail.com";
    private static final String TEST_PASSWORD = "password";
    private static final String SECRET = "secret";
    private static final Duration TOKEN_LIFETIME = Duration.ofDays(1);

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private AppJwtEncoderDecoder appJwtEncoderDecoder;
    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loginSuccess() {
        // given
        LoginRequest loginRequest = getDefaultLoginRequest();
        AuthenticationData authenticationData = getDefaultAuthenticationData();
        given(authenticationService.login(loginRequest))
                .willReturn(authenticationData);
        // when
        ResponseEntity<Void> response = authenticationController.login(loginRequest);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getHeaders().size(), 2);
        verify(authenticationService).login(loginRequest);
    }

    private LoginRequest getDefaultLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(TEST_EMAIL);
        request.setPassword(TEST_PASSWORD);
        return request;
    }

    private AppJwt getDefaultJwtToken() {
        final LocalDateTime expirationTime = LocalDateTime.now().plus(TOKEN_LIFETIME);
        return AppJwt.builder()
                .userId(TEST_ID)
                .expiration(expirationTime)
                .build();
    }

    private AuthenticationData getDefaultAuthenticationData() {
        return new AuthenticationData(appJwtEncoderDecoder.encode(getDefaultJwtToken(), SECRET), TEST_ID);
    }
}
