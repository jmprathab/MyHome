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

import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.User;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.repositories.UserRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DataLoader implements CommandLineRunner {
  private final CommunityRepository communityRepository;
  private final CommunityHouseRepository communityHouseRepository;
  private final HouseMemberRepository houseMemberRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override public void run(String... args) {
    // loadPostmanData();
  }

  private void loadPostmanData() {
    // Add user
    User savedUser = saveUser();
    // Persist community
    Community savedCommunity = saveCommunity();
    // Persist admin to repo
    User savedCommunityAdmin = saveCommunityAdmin(savedCommunity);
    // Update community with the saved admin
    savedCommunity = addAdminToCommunity(savedCommunityAdmin, savedCommunity);

    CommunityHouse savedHouse = addCommunityHouse(savedCommunity);
    savedCommunity = addHouseToCommunity(savedHouse, savedCommunity);

    HouseMember savedHouseMember = addHouseMember(savedHouse);
    savedHouse = addMemberToHouse(savedHouseMember, savedHouse);
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

  private Community addAdminToCommunity(User savedCommunityAdmin,
      Community savedCommunity) {
    savedCommunity.getAdmins().add(savedCommunityAdmin);
    return communityRepository.save(savedCommunity);
  }

  private User saveCommunityAdmin(Community savedCommunity) {
    User communityAdmin = new User()
        .withUserId(TestDataConstants.ADMIN_ID)
        .withCommunities(new HashSet<>(Arrays.asList(savedCommunity)));
    return userRepository.save(communityAdmin);
  }

  private Community saveCommunity() {
    Community community = new Community()
        .withName(TestDataConstants.COMMUNITY_NAME)
        .withCommunityId(TestDataConstants.COMMUNITY_ID)
        .withDistrict(TestDataConstants.COMMUNITY_DISTRICT);
    return communityRepository.save(community);
  }
}
