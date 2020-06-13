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
import com.myhome.controllers.dto.CommunityHouseDto;
import com.myhome.controllers.request.CreateCommunityRequest;
import com.myhome.controllers.response.CreateCommunityResponse;
import com.myhome.controllers.response.GetAdminDetailsResponse;
import com.myhome.controllers.response.GetCommunityDetailsResponse;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityAdmin;
import com.myhome.domain.CommunityHouse;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface CommunityApiMapper {
  CommunityDto createCommunityRequestToCommunityDto(CreateCommunityRequest request);

  GetCommunityDetailsResponse communityToGetCommunityDetailsResponse(Community community);

  Set<GetCommunityDetailsResponse> communitySetToGetCommunityDetailsResponseSet(
      Set<Community> communitySet);

  CreateCommunityResponse communityToCreateCommunityResponse(Community community);

  Set<GetAdminDetailsResponse> communityAdminSetToGetAdminDetailsResponseSet(
      Set<CommunityAdmin> communityAdminSet);

  CommunityHouse communityHouseDtoToCommunityHouse(CommunityHouseDto communityHouseDto);

  Set<CommunityHouseDto> communityHouseSetToCommunityHouseDtoSet(
      Set<CommunityHouse> communityHouse);
}
