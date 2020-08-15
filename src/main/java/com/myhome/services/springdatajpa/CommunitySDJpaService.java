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

package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.dto.mapper.CommunityMapper;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityAdmin;
import com.myhome.domain.CommunityHouse;
import com.myhome.repositories.CommunityAdminRepository;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.services.CommunityService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunitySDJpaService implements CommunityService {
  private final CommunityRepository communityRepository;
  private final CommunityAdminRepository communityAdminRepository;
  private final CommunityMapper communityMapper;
  private final CommunityHouseRepository communityHouseRepository;

  @Override
  public Community createCommunity(CommunityDto communityDto) {
    communityDto.setCommunityId(generateUniqueId());
    Community community = communityMapper.communityDtoToCommunity(communityDto);
    Community savedCommunity = communityRepository.save(community);
    log.trace("saved community with id[{}] to repository", savedCommunity.getId());
    return savedCommunity;
  }

  @Override
  public Set<Community> listAll(Pageable pageable) {
    Set<Community> communityListSet = new HashSet<>();
    communityRepository.findAll(pageable).forEach(communityListSet::add);
    return communityListSet;
  }

  @Override
  public Optional<List<CommunityHouse>> findCommunityHousesById(String communityId,
      Pageable pageable) {
    boolean exists = communityRepository.existsByCommunityId(communityId);
    if (exists) {
      return Optional.of(
          communityHouseRepository.findAllByCommunity_CommunityId(communityId, pageable));
    }
    return Optional.empty();
  }

  @Override
  public Optional<List<CommunityAdmin>> findCommunityAdminsById(String communityId,
      Pageable pageable) {
    boolean exists = communityRepository.existsByCommunityId(communityId);
    if (exists) {
      return Optional.of(
          communityAdminRepository.findAllByCommunities_CommunityId(communityId, pageable)
      );
    }
    return Optional.empty();
  }

  @Override public Set<Community> listAll() {
    Set<Community> communities = new HashSet<>();
    communityRepository.findAll().forEach(communities::add);
    return communities;
  }

  @Override public Optional<Community> getCommunityDetailsById(String communityId) {
    return communityRepository.findByCommunityId(communityId);
  }

  @Override
  public Optional<Community> addAdminsToCommunity(String communityId, Set<String> adminsIds) {
    Optional<Community> communitySearch = communityRepository.findByCommunityId(communityId);

    return communitySearch.map(community -> {
      adminsIds.forEach(adminId -> {
        communityAdminRepository.findByAdminId(adminId).map(admin -> {
          admin.getCommunities().add(community);
          community.getAdmins().add(communityAdminRepository.save(admin));
          return admin;
        });
      });
      return Optional.of(communityRepository.save(community));
    }).orElseGet(Optional::empty);
  }

  @Override
  public Set<String> addHousesToCommunity(String communityId, Set<CommunityHouse> houses) {
    Optional<Community> communitySearch = communityRepository.findByCommunityId(communityId);
    Set<String> addedHousesIds = communitySearch.map(community -> {
      Set<CommunityHouse> savedHouses = houses
          .stream()
          .filter(house -> communityHouseRepository.findByHouseId(house.getHouseId()) != null)
          .map(house -> house.withCommunity(community))
          .filter(house -> community.getHouses().add(house))
          .collect(Collectors.toSet());
      communityHouseRepository.saveAll(savedHouses);
      Set<String> housesIds = communityRepository.save(community).getHouses()
          .stream()
          .map(house -> house.getHouseId())
          .collect(Collectors.toSet());
      return housesIds;
    }).orElse(new HashSet<>());
    return addedHousesIds;
  }

  @Override
  public boolean removeAdminFromCommunity(String communityId, String adminId) {
    Optional<Community> communitySearch = communityRepository.findByCommunityId(communityId);
    return communitySearch.map(community -> {
      boolean adminRemoved =
          community.getAdmins().removeIf(admin -> admin.getAdminId().equals(adminId));
      if (adminRemoved) {
        communityRepository.save(community);
        return true;
      } else {
        return false;
      }
    }).orElse(false);
  }

  @Override
  public boolean deleteCommunity(String communityId) {
    return communityRepository.findByCommunityId(communityId)
        .map(community -> {
          community.getHouses()
              .stream()
              .map(CommunityHouse::getHouseId)
              .forEach(communityHouseRepository::deleteByHouseId);
          communityRepository.delete(community);
          return true;
        })
        .orElse(false);
  }

  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  public boolean removeHouseFromCommunityByHouseId(String communityId, String houseId) {
    return communityRepository.findByCommunityId(communityId)
        .map(community -> {
          CommunityHouse house = communityHouseRepository.findByHouseId(houseId);
          if (house != null && community.getHouses().contains(house)) {
            community.getHouses().remove(house);
            communityRepository.save(community);
            return true;
          } else {
            return false;
          }
        })
        .orElse(false);
  }
}
