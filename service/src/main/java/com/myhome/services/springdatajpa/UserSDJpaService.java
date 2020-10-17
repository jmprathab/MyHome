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
import com.myhome.domain.SecurityToken;
import com.myhome.domain.SecurityTokenType;
import com.myhome.domain.User;
import com.myhome.model.ForgotPasswordRequest;
import com.myhome.repositories.UserRepository;
import com.myhome.services.MailService;
import com.myhome.services.SecurityTokenService;
import com.myhome.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
  private final SecurityTokenService securityTokenService;
  private final MailService mailService;

  @Override
  public Optional<UserDto> createUser(UserDto request) {
    if (userRepository.findByEmail(request.getEmail()) == null) {
      generateUniqueUserId(request);
      encryptUserPassword(request);
      return Optional.of(createUserInRepository(request));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Set<User> listAll() {
    return listAll(PageRequest.of(0, 200));
  }

  @Override
  public Set<User> listAll(Pageable pageable) {
    return userRepository.findAll(pageable).toSet();
  }

  @Override
  public Optional<UserDto> getUserDetails(String userId) {
    Optional<User> userOptional = userRepository.findByUserIdWithCommunities(userId);
    return userOptional.map(admin -> {
      Set<String> communityIds = admin.getCommunities().stream()
          .map(community -> community.getCommunityId())
          .collect(Collectors.toSet());

      UserDto userDto = userMapper.userToUserDto(admin);
      userDto.setCommunityIds(communityIds);
      return Optional.of(userDto);
    }).orElse(Optional.empty());
  }

  @Override
  public void requestResetPassword(ForgotPasswordRequest forgotPasswordRequest) {
    if (forgotPasswordRequest.getEmail() != null) {
      SecurityToken newSecurityToken = securityTokenService.createPasswordResetToken();
      Optional<User> userOptional = userRepository.findByEmailWithTokens(forgotPasswordRequest.getEmail());
      userOptional.map(user -> {
        user.getUserTokens().add(newSecurityToken);
        userRepository.save(user);
        mailService.sendPasswordRecoverCode(user, newSecurityToken.getToken());
        return user;
      });
    }
  }

  @Override
  public boolean resetPassword(ForgotPasswordRequest passwordResetRequest) {
    if (passwordResetRequest != null) {
      Optional<User> userOptional = userRepository.findByEmailWithTokens(passwordResetRequest.getEmail());
      return userOptional.map(user -> {
        SecurityToken userPasswordResetToken = user.getUserTokens()
            .stream()
            .filter(token -> !token.isUsed()
                && token.getTokenType() == SecurityTokenType.RESET
                && token.getToken().equals(passwordResetRequest.getToken())
                && token.getExpiryDate().isAfter(LocalDate.now()))
            .findFirst()
            .orElse(null);
        if (userPasswordResetToken != null) {
          securityTokenService.useToken(userPasswordResetToken);
          user.setEncryptedPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
          user = userRepository.save(user);
          mailService.sendPasswordSuccessfullyChanged(user);
          return true;
        } else {
          return false;
        }
      }).orElse(false);
    } else {
      return false;
    }
  }

  private UserDto createUserInRepository(UserDto request) {
    User user = userMapper.userDtoToUser(request);
    User savedUser = userRepository.save(user);
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
