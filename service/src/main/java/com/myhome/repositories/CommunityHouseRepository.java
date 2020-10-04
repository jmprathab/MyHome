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

package com.myhome.repositories;

import com.myhome.domain.CommunityHouse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityHouseRepository extends PagingAndSortingRepository<CommunityHouse, Long> {

  Optional<CommunityHouse> findByHouseId(String houseId);

  @Query("from CommunityHouse house where house.houseId = :houseId")
  @EntityGraph(value = "CommunityHouse.community")
  Optional<CommunityHouse> findByHouseIdWithCommunity(@Param("houseId") String houseId);

  @Query("from CommunityHouse house where house.houseId = :houseId")
  @EntityGraph(value = "CommunityHouse.houseMembers")
  Optional<CommunityHouse> findByHouseIdWithHouseMembers(@Param("houseId") String houseId);

  @EntityGraph(value = "CommunityHouse.community")
  List<CommunityHouse> findAllByCommunity_CommunityId(String communityId, Pageable pageable);

  void deleteByHouseId(String houseId);
}
