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

package com.myhome.controllers;

import com.myhome.controllers.dto.mapper.HouseMemberMapper;
import com.myhome.controllers.request.AddHouseMemberRequest;
import com.myhome.controllers.response.AddHouseMemberResponse;
import com.myhome.controllers.response.ListHouseMembersResponse;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.services.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HouseController {
  private final CommunityHouseRepository communityHouseRepository;
  private final HouseMemberMapper houseMemberMapper;
  private final HouseService houseService;

  public HouseController(CommunityHouseRepository communityHouseRepository,
      HouseMemberMapper houseMemberMapper, HouseService houseService) {
    this.communityHouseRepository = communityHouseRepository;
    this.houseMemberMapper = houseMemberMapper;
    this.houseService = houseService;
  }

  @Operation(description = "List all members of the house given a house id")
  @GetMapping(
      path = "/houses/{houseId}/members",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<ListHouseMembersResponse> listAllMembersOfHouse(
      @PathVariable String houseId) {

    log.trace("Received request to list all members of the house with id[{}]", houseId);
    var houseMembers = communityHouseRepository.findByHouseId(houseId).getHouseMembers();
    var responseSet = houseMemberMapper.houseMemberSetToRestApiResponseHouseMemberSet(houseMembers);

    var response = new ListHouseMembersResponse();
    response.setMembers(responseSet);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(description = "Add new members to the house given a house id. Responds with member id which were added")
  @PostMapping(
      path = "/houses/{houseId}/members",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<AddHouseMemberResponse> addHouseMembers(
      @PathVariable String houseId, @Valid @RequestBody AddHouseMemberRequest request) {

    log.trace("Received request to add member to the house with id[{}]", houseId);
    var members = houseMemberMapper.houseMemberDtoSetToHouseMemberSet(request.getMembers());
    var savedHouseMembers = houseService.addHouseMembers(houseId, members);

    var response = new AddHouseMemberResponse();
    response.setMembers(
        houseMemberMapper.houseMemberSetToRestApiResponseAddHouseMemberSet(savedHouseMembers));
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(response);
  }
}
