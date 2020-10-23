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

package com.myhome.controllers.dto.mapper;

import com.myhome.controllers.dto.UserDto;
import com.myhome.domain.BaseEntity;
import com.myhome.domain.Community;
import com.myhome.domain.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Provides conversion between DTO and entity object.
 */
@Mapper
public interface UserMapper {
  User userDtoToUser(UserDto userDto);

  @Mapping(source = "communities", target = "communityIds", qualifiedByName = "communitiesToIds")
  UserDto userToUserDto(User user);

  @Named("communitiesToIds")
  static Set<String> communitiesToIds(Set<Community> communities) {
    return communities.stream()
        .map(BaseEntity::getId)
        .map(String::valueOf)
        .collect(Collectors.toSet());
  }
}
