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
import com.myhome.domain.CommunityAdmin;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.User;
import com.myhome.repositories.CommunityAdminRepository;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.repositories.UserRepository;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class DataLoader implements CommandLineRunner {
  private final CommunityRepository communityRepository;
  private final CommunityAdminRepository communityAdminRepository;
  private final CommunityHouseRepository communityHouseRepository;
  private final HouseMemberRepository houseMemberRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public DataLoader(
      CommunityRepository communityRepository,
      CommunityAdminRepository communityAdminRepository,
      CommunityHouseRepository communityHouseRepository,
      HouseMemberRepository houseMemberRepository,
      PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.communityRepository = communityRepository;
    this.communityAdminRepository = communityAdminRepository;
    this.communityHouseRepository = communityHouseRepository;
    this.houseMemberRepository = houseMemberRepository;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override public void run(String... args) throws Exception {
    loadData();
  }

  private void loadData() {
    // Add user
    var savedUser = saveUser();
    // Persist community
    var savedCommunity = saveCommunity();
    // Persist admin to repo
    var savedCommunityAdmin = saveCommunityAdmin(savedCommunity);
    // Update community with the saved admin
    savedCommunity = addAdminToCommunity(savedCommunityAdmin, savedCommunity);

    var savedHouse = addCommunityHouse(savedCommunity);
    savedCommunity = addHouseToCommunity(savedHouse, savedCommunity);

    var savedHouseMember = addHouseMember(savedHouse);
    savedHouse = addMemberToHouse(savedHouseMember, savedHouse);
  }

  private User saveUser() {
    var user = new User();
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
    var houseMember = new HouseMember();
    houseMember.setMemberId(Constants.MEMBER_ID);
    houseMember.setCommunityHouse(savedHouse);
    houseMember.setName(Constants.MEMBER_NAME);
    return houseMemberRepository.save(houseMember);
  }

  private Community addHouseToCommunity(CommunityHouse savedHouse, Community savedCommunity) {
    savedCommunity.getHouses().add(savedHouse);
    return communityRepository.save(savedCommunity);
  }

  private CommunityHouse addCommunityHouse(Community savedCommunity) {
    var house = new CommunityHouse();
    house.setCommunity(savedCommunity);
    house.setHouseId(Constants.HOUSE_ID);
    house.setName(Constants.HOUSE_NAME);
    return communityHouseRepository.save(house);
  }

  private Community addAdminToCommunity(CommunityAdmin savedCommunityAdmin,
      Community savedCommunity) {
    savedCommunity.getAdmins().add(savedCommunityAdmin);
    return communityRepository.save(savedCommunity);
  }

  private CommunityAdmin saveCommunityAdmin(Community savedCommunity) {
    var communityAdmin = new CommunityAdmin();
    var adminId = UUID.randomUUID().toString();
    communityAdmin.setAdminId(adminId);
    communityAdmin.getCommunities().add(savedCommunity);
    return communityAdminRepository.save(communityAdmin);
  }

  private Community saveCommunity() {
    var community = new Community();
    community.setName(Constants.COMMUNITY_NAME);
    community.setDistrict(Constants.COMMUNITY_DISTRICT);
    community.setCommunityId(Constants.COMMUNITY_ID);
    return communityRepository.save(community);
  }
}
