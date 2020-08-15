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

package com.myhome.services;

import com.myhome.controllers.dto.UserDto;
import com.myhome.domain.User;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;

/**
 * Interface for service layer.
 */
public interface UserService {
  Optional<UserDto> createUser(UserDto request);

  Set<User> listAll();

  Set<User> listAll(Pageable pageable);

  Optional<UserDto> getUserDetails(String userId);
}
