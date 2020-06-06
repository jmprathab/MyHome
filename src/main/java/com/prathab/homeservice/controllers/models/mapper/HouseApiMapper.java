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

package com.prathab.homeservice.controllers.models.mapper;

import com.prathab.homeservice.controllers.dto.HouseDto;
import com.prathab.homeservice.controllers.models.response.HouseDetailResponse;
import com.prathab.homeservice.domain.House;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface HouseApiMapper {
  HouseDto houseToHouseDto(House house);

  House houseDtoToHouse(HouseDto houseDto);

  HouseDetailResponse houseToHouseDetailResponse(House house);

  Set<HouseDetailResponse> houseSetToHouseDetailResponseSet(Set<House> houseSet);

  House houseDetailResponseToHouse(HouseDetailResponse houseDetailResponse);

  Set<House> houseDetailResponseSetToHouseSet(Set<HouseDetailResponse> houseDetailResponseSet);
}
