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
import com.myhome.controllers.mapper.HouseApiMapper;
import com.myhome.controllers.request.AddHouseMemberRequest;
import com.myhome.controllers.response.AddHouseMemberResponse;
import com.myhome.controllers.response.GetHouseDetailsResponse;
import com.myhome.controllers.response.ListHouseMembersResponse;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.services.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Set;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  private final HouseApiMapper houseApiMapper;

  public HouseController(CommunityHouseRepository communityHouseRepository,
      HouseMemberMapper houseMemberMapper, HouseService houseService,
      HouseApiMapper houseApiMapper) {
    this.communityHouseRepository = communityHouseRepository;
    this.houseMemberMapper = houseMemberMapper;
    this.houseService = houseService;
    this.houseApiMapper = houseApiMapper;
  }

  @Operation(description = "List all houses of the community given a community id")
  @GetMapping(
      path = "/houses",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<GetHouseDetailsResponse> listAllHouses() {
    log.trace("Received request to list all houses");
    Set<CommunityHouse> houseDetails =
        houseService.listAllHouses();
    Set<GetHouseDetailsResponse.CommunityHouse> getHouseDetailsResponseSet =
        houseApiMapper.communityHouseSetToRestApiResponseCommunityHouseSet(houseDetails);

    GetHouseDetailsResponse response = new GetHouseDetailsResponse();
    response.setHouses(getHouseDetailsResponseSet);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(description = "List all houses of the community given a community id")
  @GetMapping(
      path = "/houses/{houseId}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<GetHouseDetailsResponse> getHouseDetails(@PathVariable String houseId) {
    log.trace("Received request to get details of a house with id[{}]", houseId);
    if (!houseService.getHouseDetailsById(houseId).isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GetHouseDetailsResponse());
    }
    CommunityHouse houseDetail =
        houseService.getHouseDetailsById(houseId).get();
    GetHouseDetailsResponse.CommunityHouse getHouseDetailsResponse =
        houseApiMapper.communityHouseToRestApiResponseCommunityHouse(houseDetail);

    GetHouseDetailsResponse response = new GetHouseDetailsResponse();
    response.getHouses().add(getHouseDetailsResponse);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(description = "List all members of the house given a house id")
  @GetMapping(
      path = "/houses/{houseId}/members",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<ListHouseMembersResponse> listAllMembersOfHouse(
      @PathVariable String houseId) {

    log.trace("Received request to list all members of the house with id[{}]", houseId);
    if (!houseService.getHouseDetailsById(houseId).isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ListHouseMembersResponse());
    }
    Set<HouseMember> houseMembers =
        houseService.getHouseDetailsById(houseId).get().getHouseMembers();
    Set<ListHouseMembersResponse.HouseMember> responseSet =
        houseMemberMapper.houseMemberSetToRestApiResponseHouseMemberSet(houseMembers);

    ListHouseMembersResponse response = new ListHouseMembersResponse();
    response.setMembers(responseSet);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(
          description = "Add new members to the house given a house id. Responds with member id which were added",
          responses = {
                  @ApiResponse(responseCode = "201", description = "If members were added to house"),
                  @ApiResponse(responseCode = "404", description = "If parameters are invalid")
          })
  @PostMapping(
      path = "/houses/{houseId}/members",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<AddHouseMemberResponse> addHouseMembers(
      @PathVariable String houseId, @Valid @RequestBody AddHouseMemberRequest request) {

    log.trace("Received request to add member to the house with id[{}]", houseId);
    Set<HouseMember> members =
        houseMemberMapper.houseMemberDtoSetToHouseMemberSet(request.getMembers());
    Set<HouseMember> savedHouseMembers = houseService.addHouseMembers(houseId, members);

    if(savedHouseMembers.size() == 0 && request.getMembers().size() != 0) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } else {
      AddHouseMemberResponse response = new AddHouseMemberResponse();
      response.setMembers(houseMemberMapper.houseMemberSetToRestApiResponseAddHouseMemberSet(savedHouseMembers));
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
  }

  @Operation(description = "Deletion of member associated with a house",
      responses = {
          @ApiResponse(responseCode = "204", description = "If house member was removed from house"),
          @ApiResponse(responseCode = "404", description = "If parameters are invalid")})
  @DeleteMapping(
      path = "/houses/{houseId}/members/{memberId}"
  )
  public ResponseEntity<Void> deleteHouseMember(@PathVariable String houseId,
      @PathVariable String memberId) {
    log.trace("Received request to delete a member from house with house id[{}] and member id[{}]",
        houseId, memberId);
    boolean isMemberDeleted = houseService.deleteMemberFromHouse(houseId, memberId);
    if (isMemberDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}