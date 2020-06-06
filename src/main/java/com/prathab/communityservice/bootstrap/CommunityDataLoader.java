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

package com.prathab.communityservice.bootstrap;

import com.prathab.communityservice.domain.Community;
import com.prathab.communityservice.domain.CommunityAdmin;
import com.prathab.communityservice.repositories.CommunityAdminRepository;
import com.prathab.communityservice.repositories.CommunityRepository;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class CommunityDataLoader implements CommandLineRunner {
  private final CommunityRepository communityRepository;
  private final CommunityAdminRepository communityAdminRepository;

  public CommunityDataLoader(
      CommunityRepository communityRepository,
      CommunityAdminRepository communityAdminRepository) {
    this.communityRepository = communityRepository;
    this.communityAdminRepository = communityAdminRepository;
  }

  @Override public void run(String... args) throws Exception {
    loadData();
  }

  private void loadData() {
    // Persist community to repo
    var communityName = "MyHome default community";
    var communityDistrict = "MyHome default community district";
    var communityId = "default-community-id-for-testing";
    var community = new Community();
    community.setName(communityName);
    community.setDistrict(communityDistrict);
    community.setCommunityId(communityId);
    var savedCommunity = communityRepository.save(community);

    // Persist admin to repo
    var communityAdmin = new CommunityAdmin();
    var adminId = UUID.randomUUID().toString();
    communityAdmin.setAdminId(adminId);
    communityAdmin.getCommunities().add(savedCommunity);
    var savedCommunityAdmin = communityAdminRepository.save(communityAdmin);

    // Update community with the saved admin
    savedCommunity.getAdmins().add(savedCommunityAdmin);
    communityRepository.save(savedCommunity);
  }
}
