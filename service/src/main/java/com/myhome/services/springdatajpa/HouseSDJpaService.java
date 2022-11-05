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

import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseRental;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.repositories.HouseHistoryRepository;
import com.myhome.services.HouseService;
import java.time.OffsetDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Service
public class HouseSDJpaService implements HouseService {
  private final HouseMemberRepository houseMemberRepository;
  private final HouseHistoryRepository houseHistoryRepository;
  private final CommunityHouseRepository communityHouseRepository;

  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  @Override
  public Set<CommunityHouse> listAllHouses() {
    Set<CommunityHouse> communityHouses = new HashSet<>();
    communityHouseRepository.findAll().forEach(communityHouses::add);
    return communityHouses;
  }

  @Override
  public Set<CommunityHouse> listAllHouses(Pageable pageable) {
    Set<CommunityHouse> communityHouses = new HashSet<>();
    communityHouseRepository.findAll(pageable).forEach(communityHouses::add);
    return communityHouses;
  }

  @Override public Set<HouseMember> addHouseMembers(String houseId, Set<HouseMember> houseMembers) {
    Optional<CommunityHouse> communityHouseOptional =
       communityHouseRepository.findByHouseIdWithHouseMembers(houseId);
    return communityHouseOptional.map(communityHouse -> {
      Set<HouseMember> savedMembers = new HashSet<>();
      houseMembers.forEach(member -> member.setMemberId(generateUniqueId()));
      houseMembers.forEach(member -> member.setCommunityHouse(communityHouse));
      houseMemberRepository.saveAll(houseMembers).forEach(savedMembers::add);

      communityHouse.getHouseMembers().addAll(savedMembers);
      communityHouseRepository.save(communityHouse);
      return savedMembers;
    }).orElse(new HashSet<>());
  }

  @Override
  public boolean deleteMemberFromHouse(String houseId, String memberId) {
    Optional<CommunityHouse> communityHouseOptional =
        communityHouseRepository.findByHouseIdWithHouseMembers(houseId);
    return communityHouseOptional.map(communityHouse -> {
      boolean isMemberRemoved = false;
      if (!CollectionUtils.isEmpty(communityHouse.getHouseMembers())) {
        Set<HouseMember> houseMembers = communityHouse.getHouseMembers();
        for (HouseMember member : houseMembers) {
          if (member.getMemberId().equals(memberId)) {
            houseMembers.remove(member);
            communityHouse.setHouseMembers(houseMembers);
            communityHouseRepository.save(communityHouse);
            member.setCommunityHouse(null);
            houseMemberRepository.save(member);
            isMemberRemoved = true;
            break;
          }
        }
      }
      return isMemberRemoved;
    }).orElse(false);
  }

  @Override
  public Optional<CommunityHouse> getHouseDetailsById(String houseId) {
    return communityHouseRepository.findByHouseId(houseId);
  }

  @Override
  public Optional<List<HouseMember>> getHouseMembersById(String houseId, Pageable pageable) {
    return Optional.ofNullable(
        houseMemberRepository.findAllByCommunityHouse_HouseId(houseId, pageable)
    );
  }

  @Override
  public Optional<List<HouseMember>> listHouseMembersForHousesOfUserId(String userId,
      Pageable pageable) {
    return Optional.ofNullable(
        houseMemberRepository.findAllByCommunityHouse_Community_Admins_UserId(userId, pageable)
    );
  }

  @Override
  public Optional<List<HouseRental>> listHouseRentalsForHouseId(String houseId, Pageable pageable) {
    return houseHistoryRepository.findAllByCommunityHouse_HouseId(houseId, pageable);
  }

  @Override
  public Optional<HouseRental> createRentalForHouseId(String houseId, String houseMemberId, OffsetDateTime bookingFromDate, OffsetDateTime bookingToDate) {
    Optional<CommunityHouse> communityHouseOptional =
        communityHouseRepository.findByHouseIdWithHouseMembers(houseId);
    Optional<HouseMember> houseMemberOptional =
        houseMemberRepository.findByMemberId(houseMemberId);
    if(communityHouseOptional.isPresent() && houseMemberOptional.isPresent()){
      return Optional.of(
          houseHistoryRepository.save(new HouseRental(
                  houseId,
                  houseMemberId,
                  bookingFromDate,
                  bookingToDate,
                  null,
                  null,
                  communityHouseOptional.get(),
                  houseMemberOptional.get()
              ))
      );
    }else { return Optional.empty(); }
  }
}
