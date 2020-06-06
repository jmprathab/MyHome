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

package com.prathab.homeservice.services.springdatajpa;

import com.prathab.homeservice.controllers.dto.HouseDto;
import com.prathab.homeservice.controllers.models.mapper.HouseApiMapper;
import com.prathab.homeservice.domain.House;
import com.prathab.homeservice.repositories.HouseRepository;
import com.prathab.homeservice.services.HouseService;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class HouseSDJpaService implements HouseService {
  private final HouseRepository houseRepository;
  private final HouseApiMapper houseApiMapper;

  public HouseSDJpaService(HouseRepository houseRepository,
      HouseApiMapper houseApiMapper) {
    this.houseRepository = houseRepository;
    this.houseApiMapper = houseApiMapper;
  }

  @Override public House addHouse(HouseDto houseDto) {
    var house = houseApiMapper.houseDtoToHouse(houseDto);
    house.setHouseId(generateUniqueHouseId());
    return houseRepository.save(house);
  }

  private String generateUniqueHouseId() {
    return UUID.randomUUID().toString();
  }

  @Override public Set<House> findAllHouses() {
    var houseSet = new HashSet<House>();
    houseRepository.findAll().forEach(houseSet::add);
    return houseSet;
  }
}
