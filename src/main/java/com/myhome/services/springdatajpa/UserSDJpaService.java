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

package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.UserMapper;
import com.myhome.repositories.UserRepository;
import com.myhome.services.UserService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implements {@link UserService} and uses Spring Data JPA repository to does its work.
 */
@Service
@Slf4j
public class UserSDJpaService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserSDJpaService(UserRepository userRepository,
      UserMapper userMapper,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override public UserDto createUser(UserDto request) {
    generateUniqueUserId(request);
    encryptUserPassword(request);
    return createUserInRepository(request);
  }

  private UserDto createUserInRepository(UserDto request) {
    var user = userMapper.userDtoToUser(request);
    var savedUser = userRepository.save(user);
    log.trace("saved user with id[{}] to repository", savedUser.getId());
    return userMapper.userToUserDto(savedUser);
  }

  private void encryptUserPassword(UserDto request) {
    request.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
  }

  private void generateUniqueUserId(UserDto request) {
    request.setUserId(UUID.randomUUID().toString());
  }
}
