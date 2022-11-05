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

import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.myhome.domain.HouseRental;
import org.springframework.data.domain.Pageable;

public interface HouseService {
  Set<CommunityHouse> listAllHouses();

  Set<CommunityHouse> listAllHouses(Pageable pageable);

  Set<HouseMember> addHouseMembers(String houseId, Set<HouseMember> houseMembers);

  boolean deleteMemberFromHouse(String houseId, String memberId);

  Optional<CommunityHouse> getHouseDetailsById(String houseId);

  Optional<List<HouseMember>> getHouseMembersById(String houseId, Pageable pageable);

  Optional<List<HouseMember>> listHouseMembersForHousesOfUserId(String userId, Pageable pageable);

  Optional<List<HouseRental>> listHouseRentalsForHouseId(String houseId, Pageable pageable);

  Optional<HouseRental> createRentalForHouseId(String houseId, String houseMemberId, OffsetDateTime bookingFromDate, OffsetDateTime bookingToDate);

}
