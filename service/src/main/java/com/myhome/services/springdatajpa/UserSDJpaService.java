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
import com.myhome.domain.User;
import com.myhome.repositories.UserRepository;
import com.myhome.services.UserService;
import com.myhome.services.springdatajpa.exceptions.EmailAlreadyExistsException;
import com.myhome.services.springdatajpa.exceptions.UserNotFoundException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implements {@link UserService} and uses Spring Data JPA repository to does its work.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserSDJpaService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override public void createUser(UserDto dto) {
    verifyEmailUniqueness(dto);
    generateUniqueUserId(dto);
    encryptUserPassword(dto);
    saveUser(dto);
    }

  @Override public Set<User> listAll() {
    return listAll(PageRequest.of(0, 200));
  }

  @Override public Set<User> listAll(Pageable pageable) {
    return userRepository.findAll(pageable).toSet();
  }

  @Override
  public UserDto getUserDetails(String userId) {
    User user = userRepository.findByUserIdWithCommunities(userId)
      .orElseThrow(() -> new UserNotFoundException(userId));
    return userMapper.userToUserDto(user);
  }

  private void saveUser(UserDto request) {
    User savedUser = userRepository.save(userMapper.userDtoToUser(request));
    log.trace("saved user with id[{}] to repository", savedUser.getId());
  }

  private void encryptUserPassword(UserDto request) {
    request.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
  }

  private void generateUniqueUserId(UserDto request) {
    request.setUserId(UUID.randomUUID().toString());
  }

  private void verifyEmailUniqueness(UserDto request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExistsException();
    }
  }
}
