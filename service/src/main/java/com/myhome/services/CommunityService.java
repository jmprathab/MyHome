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

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface CommunityService {
  Community createCommunity(CommunityDto communityDto);

  Set<Community> listAll();

  Set<Community> listAll(Pageable pageable);

  Optional<Community> getCommunityDetailsById(String communityId);

  Optional<List<CommunityHouse>> findCommunityHousesById(String communityId, Pageable pageable);

  Optional<List<User>> findCommunityAdminsById(String communityId, Pageable pageable);

  Optional<User> findCommunityAdminById(String adminId);

  Optional<Community> getCommunityDetailsByIdWithAdmins(String communityId);

  Optional<Community> addAdminsToCommunity(String communityId, Set<String> admins);

  Set<String> addHousesToCommunity(String communityId, Set<CommunityHouse> houses);

  boolean removeHouseFromCommunityByHouseId(Community community, String houseId);

  boolean deleteCommunity(String communityId);

  boolean removeAdminFromCommunity(String communityId, String adminId);
}
