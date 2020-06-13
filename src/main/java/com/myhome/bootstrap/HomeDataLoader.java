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

import com.myhome.domain.House;
import com.myhome.repositories.CommunityRepository;
import com.myhome.repositories.HouseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class HomeDataLoader implements CommandLineRunner {
  private final HouseRepository houseRepository;
  private final CommunityRepository communityRepository;

  public HomeDataLoader(HouseRepository houseRepository,
      CommunityRepository communityRepository) {
    this.houseRepository = houseRepository;
    this.communityRepository = communityRepository;
  }

  @Override public void run(String... args) throws Exception {
    var houseName = "MyHome default house";
    var houseId = "default-house-id-for-testing";
    var communityId = "default-community-id-for-testing";

    var community = communityRepository.findByCommunityId(communityId);
    var house = new House();
    house.setHouseId(houseId);
    house.setName(houseName);
    house.setCommunity(community);
    houseRepository.save(house);
  }
}
