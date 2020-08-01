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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunitySDJpaService implements CommunityService {
  private final CommunityRepository communityRepository;
  private final CommunityAdminRepository communityAdminRepository;
  private final CommunityMapper communityMapper;
  private final CommunityHouseRepository communityHouseRepository;

  @Override public Community createCommunity(CommunityDto communityDto) {
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
  public Optional<Community> getCommunityDetailsById(String communityId) {
    return Optional.ofNullable(communityRepository.findByCommunityId(communityId));
  }

  @Override
  public Optional<Community> getCommunityDetailsById(String communityId, Pageable pageable) {
    return Optional.ofNullable(communityRepository.findByCommunityId(communityId, pageable));
  }

  @Override public Community addAdminsToCommunity(String communityId, Set<String> admins) {
    if (!getCommunityDetailsById(communityId).isPresent()) return new Community();
    Community community = communityRepository.findByCommunityId(communityId);

    Set<CommunityAdmin> savedAdminSet = new HashSet<>();
    admins.forEach(s -> {
      CommunityAdmin admin = new CommunityAdmin();
      admin.setAdminId(s);
      admin.getCommunities().add(community);
      savedAdminSet.add(communityAdminRepository.save(admin));
    });
    community.getAdmins().addAll(savedAdminSet);
    return communityRepository.save(community);
  }

  // Returns houseId which was added to the community
  @Override
  public Set<String> addHousesToCommunity(String communityId, Set<CommunityHouse> houses) {
    if (!getCommunityDetailsById(communityId).isPresent()) return new HashSet<>();
    Community community = communityRepository.findByCommunityId(communityId);
    houses.stream().map(house -> {
      house.setHouseId(generateUniqueId());
      return house;
    }).forEach(communityHouse -> communityHouse.setCommunity(community));
    Set<CommunityHouse> savedHouses = new HashSet<>();
    communityHouseRepository.saveAll(houses).forEach(savedHouses::add);
    community.getHouses().addAll(savedHouses);
    communityRepository.save(community);

    Set<String> houseIds = new HashSet<>(savedHouses.size());
    savedHouses.forEach(communityHouse -> houseIds.add(communityHouse.getHouseId()));
    return houseIds;
  }

  @Override
  public Optional<Community> deleteAdminFromCommunity(String communityId, String adminId) {
    final Community community = communityRepository.findByCommunityId(communityId);
    if (community == null || community.getAdmins().isEmpty()) {
      return Optional.empty();
    }
    Set<CommunityAdmin> communityAdmins = community.getAdmins();
    boolean removed =
        communityAdmins.removeIf(communityAdmin -> communityAdmin.getAdminId().equals(adminId));
    if (!removed) {
      return Optional.empty();
    }
    community.setAdmins(communityAdmins);
    Community savedCommunity = communityRepository.save(community);
    return Optional.of(savedCommunity);
  }

  @Override
  @Transactional
  public Integer deleteCommunity(String communityId) {

    getCommunityDetailsById(communityId).ifPresent(community -> {
      community.getHouses()
          .stream()
          .map(CommunityHouse::getHouseId)
          .forEach(communityHouseRepository::deleteByHouseId);
    });
    return communityRepository.deleteByCommunityId(communityId);
  }

  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  public void deleteHouseFromCommunityByHouseId(String houseId) {
    communityHouseRepository.deleteByHouseId(houseId);
  }
}
