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

package com.myhome.bootstrap;

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityAdmin;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.User;
import com.myhome.repositories.CommunityAdminRepository;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.CommunityService;
import com.myhome.services.HouseService;
import com.myhome.services.UserService;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DataLoader implements CommandLineRunner {
  private final CommunityService communityService;
  private final UserService userService;
  private final HouseService houseService;
  private final CommunityRepository communityRepository;
  private final CommunityAdminRepository communityAdminRepository;
  private final CommunityHouseRepository communityHouseRepository;
  private final HouseMemberRepository houseMemberRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override public void run(String... args) throws Exception {
//    loadPostmanData();
//    loadData();
  }

  private void loadPostmanData() {
    // Add user
    User savedUser = saveUser();
    // Persist community
    Community savedCommunity = saveCommunity();
    // Persist admin to repo
    CommunityAdmin savedCommunityAdmin = saveCommunityAdmin(savedCommunity);
    // Update community with the saved admin
    savedCommunity = addAdminToCommunity(savedCommunityAdmin, savedCommunity);

    CommunityHouse savedHouse = addCommunityHouse(savedCommunity);
    savedCommunity = addHouseToCommunity(savedHouse, savedCommunity);

    HouseMember savedHouseMember = addHouseMember(savedHouse);
    savedHouse = addMemberToHouse(savedHouseMember, savedHouse);
  }

  private void loadData() {
    // Create and add communities
    for (int i = 1; i <= 50; i++) {
      CommunityDto dto = new CommunityDto();
      dto.setName(String.format(TestDataConstants.TEST_COMMUNITY_NAME, i));
      dto.setDistrict(String.format(TestDataConstants.TEST_COMMUNITY_DISTRICT));
      communityService.createCommunity(dto);
    }

    // Create and add users
    for (int i = 1; i <= 50; i++) {
      UserDto dto = new UserDto();
      dto.setEmail(String.format(TestDataConstants.TEST_USER_EMAIL_ID, i));
      dto.setName(String.format(TestDataConstants.TEST_USER_NAME, i));
      dto.setPassword(String.format(TestDataConstants.TEST_USER_PASSWORD));
      userService.createUser(dto);
    }

    // Fetch first 10 users
    final Set<String> userNames = new HashSet<>();
    for (int i = 1; i <= 10; i++) {
      userNames.add(String.format(TestDataConstants.TEST_USER_EMAIL_ID, i));
    }
    Set<String> users = userService.listAll()
        .stream()
        .filter(user -> userNames.contains(user.getEmail()))
        .map(User::getUserId)
        .collect(Collectors.toSet());

    // Users 1 to 10 are admins of each community.
    communityService.listAll()
        .stream()
        .map(Community::getCommunityId)
        .forEach(c -> communityService.addAdminsToCommunity(c, users));

    // Add 10 houses to each community
    Map<String, Set<CommunityHouse>> communityHouseMap = new HashMap<>();
    Set<Community> communities = communityService.listAll();
    for (Community community : communities) {
      Set<CommunityHouse> houses = new HashSet<>();
      for (int i = 1; i <= 10; i++) {
        CommunityHouse newHouse = new CommunityHouse();
        newHouse.setName(String.format(TestDataConstants.TEST_HOUSE_NAME, i));
        // newHouse.setHouseId(UUID.randomUUID().toString());
        houses.add(newHouse);
      }
      communityHouseMap.put(community.getCommunityId(), houses);
    }
    communityHouseMap.forEach(communityService::addHousesToCommunity);

    // Fetch users 11 to 20
    final Set<String> houseMemberUsers = new HashSet<>();
    for (int i = 11; i <= 20; i++) {
      houseMemberUsers.add(String.format(TestDataConstants.TEST_USER_EMAIL_ID, i));
    }
    List<User> houseMemberUsersEntity = userService.listAll()
        .stream()
        .filter(user -> houseMemberUsers.contains(user.getEmail()))
        .sorted(Comparator.comparing(User::getName))
        .collect(Collectors.toList());

    // Add 10 members to each house
    Map<String, Set<HouseMember>> houseMemberMap = new HashMap<>();
    Set<CommunityHouse> houses = houseService.listAllHouses();
    for (CommunityHouse house : houses) {
      Set<HouseMember> members = new HashSet<>();
      for (int i = 0; i < 10; i++) {
        HouseMember newMember = new HouseMember();
        String name = houseMemberUsersEntity.get(i).getName();
        String userId = houseMemberUsersEntity.get(i).getUserId();
        newMember.setName(name);
        newMember.setMemberId(userId);
        members.add(newMember);
      }
      houseMemberMap.put(house.getHouseId(), members);
    }
    houseMemberMap.forEach(houseService::addHouseMembers);
  }

  private User saveUser() {
    User user = new User();
    user.setName("Test");
    user.setEmail("test@test.com");
    user.setUserId(UUID.randomUUID().toString());
    user.setEncryptedPassword(passwordEncoder.encode("testtest"));
    return userRepository.save(user);
  }

  private CommunityHouse addMemberToHouse(HouseMember savedHouseMember, CommunityHouse savedHouse) {
    savedHouse.getHouseMembers().add(savedHouseMember);
    return communityHouseRepository.save(savedHouse);
  }

  private HouseMember addHouseMember(CommunityHouse savedHouse) {
    HouseMember houseMember = new HouseMember();
    houseMember.setMemberId(TestDataConstants.MEMBER_ID);
    houseMember.setCommunityHouse(savedHouse);
    houseMember.setName(TestDataConstants.MEMBER_NAME);
    return houseMemberRepository.save(houseMember);
  }

  private Community addHouseToCommunity(CommunityHouse savedHouse, Community savedCommunity) {
    savedCommunity.getHouses().add(savedHouse);
    return communityRepository.save(savedCommunity);
  }

  private CommunityHouse addCommunityHouse(Community savedCommunity) {
    CommunityHouse house = new CommunityHouse();
    house.setCommunity(savedCommunity);
    house.setHouseId(TestDataConstants.HOUSE_ID);
    house.setName(TestDataConstants.HOUSE_NAME);
    return communityHouseRepository.save(house);
  }

  private Community addAdminToCommunity(CommunityAdmin savedCommunityAdmin,
      Community savedCommunity) {
    savedCommunity.getAdmins().add(savedCommunityAdmin);
    return communityRepository.save(savedCommunity);
  }

  private CommunityAdmin saveCommunityAdmin(Community savedCommunity) {
    CommunityAdmin communityAdmin = new CommunityAdmin();
    String adminId = UUID.randomUUID().toString();
    communityAdmin.setAdminId(adminId);
    communityAdmin.getCommunities().add(savedCommunity);
    return communityAdminRepository.save(communityAdmin);
  }

  private Community saveCommunity() {
    Community community = new Community();
    community.setName(TestDataConstants.COMMUNITY_NAME);
    community.setDistrict(TestDataConstants.COMMUNITY_DISTRICT);
    community.setCommunityId(TestDataConstants.COMMUNITY_ID);
    return communityRepository.save(community);
  }
}
