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

package com.prathab.communityservice.services.springdatajpa;

import com.prathab.communityservice.domain.Community;
import com.prathab.communityservice.dto.CommunityAdminDto;
import com.prathab.communityservice.dto.mapper.CommunityAdminMapper;
import com.prathab.communityservice.repositories.CommunityAdminRepository;
import com.prathab.communityservice.services.CommunityAdminService;

public class CommunityAdminSDJpaService implements CommunityAdminService {
  private final CommunityAdminRepository communityAdminRepository;
  private final CommunityAdminMapper communityAdminMapper;

  public CommunityAdminSDJpaService(
      CommunityAdminRepository communityAdminRepository,
      CommunityAdminMapper communityAdminMapper) {
    this.communityAdminRepository = communityAdminRepository;
    this.communityAdminMapper = communityAdminMapper;
  }

  @Override
  public Community addCommunityAdmin(String communityId, CommunityAdminDto communityAdminDto) {
    var communityAdmin = communityAdminMapper.communityAdminDtoToCommunityAdmin(communityAdminDto);
    var savedCommunityAdmin = communityAdminRepository.save(communityAdmin);
    // TODO complete this
    return null;
  }
}
