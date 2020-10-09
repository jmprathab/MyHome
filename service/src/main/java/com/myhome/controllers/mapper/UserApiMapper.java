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

package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.UserDto;
import com.myhome.domain.User;
import com.myhome.model.CreateUserRequest;
import com.myhome.model.CreateUserResponse;
import com.myhome.model.GetUserDetailsResponseUser;
import java.util.Set;
import org.mapstruct.Mapper;

/**
 * Interface to automatic conversion by Mapstruct
 */
@Mapper
public interface UserApiMapper {

  UserDto createUserRequestToUserDto(CreateUserRequest createUserRequest);

  Set<GetUserDetailsResponseUser> userSetToRestApiResponseUserSet(
      Set<User> userSet);

  CreateUserResponse userDtoToCreateUserResponse(UserDto userDto);

  GetUserDetailsResponseUser userDtoToGetUserDetailsResponse(UserDto userDto);
}
