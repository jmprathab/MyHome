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

package com.prathab.homeservice.controllers;

import com.prathab.homeservice.controllers.models.mapper.HouseApiMapper;
import com.prathab.homeservice.controllers.models.request.ListAllHouseRequestBody;
import com.prathab.homeservice.controllers.models.response.ListAllHouseResponse;
import com.prathab.homeservice.services.HouseService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HouseController {
  private final HouseService houseService;
  private final HouseApiMapper houseApiMapper;

  public HouseController(HouseService houseService,
      HouseApiMapper houseApiMapper) {
    this.houseService = houseService;
    this.houseApiMapper = houseApiMapper;
  }

  @GetMapping("/houses/status")
  public String status() {
    return "Working";
  }

  @GetMapping(
      path = "/houses",
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<ListAllHouseResponse> listAllHouses(@RequestBody @Valid
      ListAllHouseRequestBody request) {
    var houseSet = houseService.findAllHouses();
    var houseDetailResponseSet = houseApiMapper.houseSetToHouseDetailResponseSet(houseSet);
    var listAllHouseResponse = new ListAllHouseResponse();
    listAllHouseResponse.setHouseDetails(houseDetailResponseSet);
    return ResponseEntity.status(HttpStatus.OK).body(listAllHouseResponse);
  }
}
