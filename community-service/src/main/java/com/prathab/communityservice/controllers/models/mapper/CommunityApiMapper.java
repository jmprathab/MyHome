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

package com.prathab.communityservice.controllers.models.mapper;

import com.prathab.communityservice.controllers.models.request.CreateCommunityRequest;
import com.prathab.communityservice.controllers.models.response.CreateCommunityResponse;
import com.prathab.communityservice.controllers.models.response.GetCommunityDetailsResponse;
import com.prathab.communityservice.dto.CommunityDto;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface CommunityApiMapper {
  CommunityDto createCommunityRequestToCommunityDto(CreateCommunityRequest request);

  CreateCommunityResponse communityDtoToCreateCommunityResponse(CommunityDto communityDto);

  Set<GetCommunityDetailsResponse> communityDtoSetToGetCommunityDetailsResponseSet(
      Set<CommunityDto> communityDtoSet);

  GetCommunityDetailsResponse communityDtoToGetCommunityDetailsResponse(
      CommunityDto communityDtoSet);
}
