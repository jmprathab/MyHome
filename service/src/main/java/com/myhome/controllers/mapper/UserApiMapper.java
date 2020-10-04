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
import com.myhome.controllers.request.CreateUserRequest;
import com.myhome.controllers.response.CreateUserResponse;
import com.myhome.controllers.response.GetUserDetailsResponse;
import com.myhome.domain.Community;
import com.myhome.domain.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Interface to automatic conversion by Mapstruct
 */
@Mapper
public interface UserApiMapper {

  @Named("communitySetToIdsSet")
  static Set<String> communityObjectSetToCommunityIdSet(Set<Community> communities) {
    return communities.stream()
        .map(community -> community.getCommunityId())
        .collect(Collectors.toSet());
  }

  UserDto createUserRequestToUserDto(CreateUserRequest createUserRequest);

  Set<GetUserDetailsResponse.User> userSetToRestApiResponseUserSet(
      Set<User> userSet);

  CreateUserResponse userDtoToCreateUserResponse(UserDto userDto);

  GetUserDetailsResponse.User userDtoToGetUserDetailsResponse(UserDto userDto);

  @Mapping(source = "communities", target = "communityIds", qualifiedByName = "communitySetToIdsSet")
  GetUserDetailsResponse.User userToRestApiUser(User user);
}
