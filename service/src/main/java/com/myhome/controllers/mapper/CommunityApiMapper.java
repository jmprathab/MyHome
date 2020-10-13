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

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.User;
import com.myhome.model.CommunityHouseName;
import com.myhome.model.CreateCommunityRequest;
import com.myhome.model.CreateCommunityResponse;
import com.myhome.model.GetCommunityDetailsResponseCommunity;
import com.myhome.model.GetHouseDetailsResponseCommunityHouse;
import com.myhome.model.ListCommunityAdminsResponseCommunityAdmin;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommunityApiMapper {
  CommunityDto createCommunityRequestToCommunityDto(CreateCommunityRequest request);

  GetCommunityDetailsResponseCommunity communityToRestApiResponseCommunity(
      Community community);

  Set<GetCommunityDetailsResponseCommunity> communitySetToRestApiResponseCommunitySet(
      Set<Community> communitySet);

  CreateCommunityResponse communityToCreateCommunityResponse(Community community);

  Set<ListCommunityAdminsResponseCommunityAdmin> communityAdminSetToRestApiResponseCommunityAdminSet(
      Set<User> communityAdminSet);

  @Mapping(source = "userId", target = "adminId")
  ListCommunityAdminsResponseCommunityAdmin userAdminToResponseAdmin(User user);

  Set<CommunityHouse> communityHouseNamesSetToCommunityHouseSet(
      Set<CommunityHouseName> communityHouseNamesSet);

  Set<GetHouseDetailsResponseCommunityHouse> communityHouseSetToRestApiResponseCommunityHouseSet(
      Set<CommunityHouse> communityHouse);
}
