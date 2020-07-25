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

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.mapper.CommunityApiMapper;
import com.myhome.controllers.request.AddCommunityAdminRequest;
import com.myhome.controllers.request.AddCommunityHouseRequest;
import com.myhome.controllers.request.CreateCommunityRequest;
import com.myhome.controllers.response.AddCommunityAdminResponse;
import com.myhome.controllers.response.AddCommunityHouseResponse;
import com.myhome.controllers.response.CreateCommunityResponse;
import com.myhome.controllers.response.GetCommunityDetailsResponse;
import com.myhome.controllers.response.GetHouseDetailsResponse;
import com.myhome.controllers.response.ListCommunityAdminsResponse;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityAdmin;
import com.myhome.domain.CommunityHouse;
import com.myhome.services.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

/**
 * REST Controller which provides endpoints for managing community
 */
@RestController
@Slf4j
public class CommunityController {
  private final CommunityService communityService;
  private final CommunityApiMapper communityApiMapper;

  public CommunityController(
      CommunityService communityService,
      CommunityApiMapper communityApiMapper) {
    this.communityService = communityService;
    this.communityApiMapper = communityApiMapper;
  }

  @Operation(description = "Create a new community")
  @PostMapping(
      path = "/communities",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<CreateCommunityResponse> createCommunity(@Valid @RequestBody
      CreateCommunityRequest request) {
    log.trace("Received create community request");
    CommunityDto requestCommunityDto =
        communityApiMapper.createCommunityRequestToCommunityDto(request);
    Community createdCommunity = communityService.createCommunity(requestCommunityDto);
    CreateCommunityResponse createdCommunityResponse =
        communityApiMapper.communityToCreateCommunityResponse(createdCommunity);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCommunityResponse);
  }

  @Operation(description = "List all communities which are registered")
  @GetMapping(
      path = "/communities",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<GetCommunityDetailsResponse> listAllCommunity() {
    log.trace("Received request to list all community");
    Set<Community> communityDetails = communityService.listAll();
    Set<GetCommunityDetailsResponse.Community> communityDetailsResponse =
        communityApiMapper.communitySetToRestApiResponseCommunitySet(communityDetails);

    GetCommunityDetailsResponse response = new GetCommunityDetailsResponse();
    response.getCommunities().addAll(communityDetailsResponse);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(description = "Get details about the community given a community id")
  @GetMapping(
      path = "/communities/{communityId}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<GetCommunityDetailsResponse> listCommunityDetails(
      @PathVariable String communityId) {
    log.trace("Received request to get details about community with id[{}]", communityId);
    Community communityDetails = communityService.getCommunityDetailsById(communityId);
    GetCommunityDetailsResponse.Community communityDetailsResponse =
        communityApiMapper.communityToRestApiResponseCommunity(communityDetails);

    GetCommunityDetailsResponse response = new GetCommunityDetailsResponse();
    response.getCommunities().add(communityDetailsResponse);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(description = "List all admins of the community given a community id")
  @GetMapping(
      path = "/communities/{communityId}/admins",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<ListCommunityAdminsResponse> listCommunityAdmins(
      @PathVariable String communityId) {
    log.trace("Received request to list all admins of community with id[{}]", communityId);
    List<CommunityAdmin> adminDetails =
        communityService.getCommunityDetailsById(communityId).getAdmins();
    List<ListCommunityAdminsResponse.CommunityAdmin> communityAdminSet =
        communityApiMapper.communityAdminSetToRestApiResponseCommunityAdminSet(adminDetails);

    ListCommunityAdminsResponse response = new ListCommunityAdminsResponse();
    response.getAdmins().addAll(communityAdminSet);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(description = "List all houses of the community given a community id")
  @GetMapping(
      path = "/communities/{communityId}/houses",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<GetHouseDetailsResponse> listCommunityHouses(
      @PathVariable String communityId, @PathVariable(required = false) String sort) {
    log.trace("Received request to list all houses of community with id[{}]", communityId);

    if (sort != null) {
      if (!sort.contentEquals("asc") || !sort.contentEquals("desc")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
    }
    if (sort == null) {
      sort = "asc";
    }

    List<CommunityHouse> houseDetails =
        communityService.getCommunityDetailsById(communityId).getHouses();
    List<GetHouseDetailsResponse.CommunityHouse> getHouseDetailsResponseSet =
        communityApiMapper.communityHouseSetToRestApiResponseCommunityHouseSet(houseDetails);

    boolean isAscendingSort = sort.contentEquals("asc");
    Comparator<GetHouseDetailsResponse.CommunityHouse> comparator =
        isAscendingSort ? Comparator.comparing(GetHouseDetailsResponse.CommunityHouse::getName)
            : Comparator.comparing(GetHouseDetailsResponse.CommunityHouse::getName).reversed();

    getHouseDetailsResponseSet = getHouseDetailsResponseSet.stream().sorted(comparator).collect(
        Collectors.toList());
    GetHouseDetailsResponse response = new GetHouseDetailsResponse();
    response.setHouses(getHouseDetailsResponseSet);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(description = "Add a new admin to the community given a community id")
  @PostMapping(
      path = "/communities/{communityId}/admins",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<AddCommunityAdminResponse> addCommunityAdmin(
      @PathVariable String communityId, @Valid @RequestBody
      AddCommunityAdminRequest request) {
    log.trace("Received request to add admin to community with id[{}]", communityId);
    Community community = communityService.addAdminsToCommunity(communityId, request.getAdmins());
    AddCommunityAdminResponse response = new AddCommunityAdminResponse();
    Set<String> adminsSet =
        community.getAdmins().stream().map(CommunityAdmin::getAdminId).collect(Collectors.toSet());
    response.setAdmins(adminsSet);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(description = "Add a new house to the community given a community id")
  @PostMapping(
      path = "/communities/{communityId}/houses",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<AddCommunityHouseResponse> addCommunityHouse(
      @PathVariable String communityId, @Valid @RequestBody
      AddCommunityHouseRequest request) {
    log.trace("Received request to add house to community with id[{}]", communityId);

    Set<CommunityHouse> communityHouses =
        communityApiMapper.communityHouseDtoSetToCommunityHouseSet(request.getHouses());
    Set<String> houseIds = communityService.addHousesToCommunity(communityId, communityHouses);
    AddCommunityHouseResponse response = new AddCommunityHouseResponse();
    response.setHouses(houseIds);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(description = "Deletion of house from the community given a community id and a house id")
  @DeleteMapping(
      path = "/communities/{communityId}/houses/{houseId}"
  )
  public ResponseEntity<Void> deleteCommunityHouse(
      @PathVariable String communityId, @PathVariable String houseId
  ) {
    communityService.deleteHouseFromCommunityByHouseId(houseId);
    log.trace(
        "Received request to delete house with id[{}] from community with id[{}]",
        houseId, communityId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(description = "Deletion of admin associated with a community"
      , responses = {@ApiResponse(responseCode = "204", description = "If admin was removed"),
      @ApiResponse(responseCode = "404", description = "If parameters are invalid")})
  @DeleteMapping(
      path = "/communities/{communityId}/admins/{adminId}"
  )
  public ResponseEntity<Void> deleteAdminFromCommunity(
      @PathVariable String communityId, @PathVariable String adminId) {
    log.trace(
        "Received request to delete an admin from community with community id[{}] and admin id[{}]",
        communityId, adminId);
    Optional<Community> community = communityService.deleteAdminFromCommunity(communityId, adminId);
    if (community.isPresent()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Operation(description = "Deletion community with given community id",
      responses = {@ApiResponse(responseCode = "204", description = "If community was removed"),
          @ApiResponse(responseCode = "404", description = "If parameters are invalid")})
  @DeleteMapping(
      path = "/communities/{communityId}"
  )
  public ResponseEntity<Void> deleteCommunity(@PathVariable String communityId) {
    log.trace("Received delete community request");
    Integer isDeleted = communityService.deleteCommunity(communityId);
    if (isDeleted == 1) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
