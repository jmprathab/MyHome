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
import com.myhome.controllers.dto.CommunityHouseName;
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
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.User;
import com.myhome.services.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequiredArgsConstructor
@RestController
@Slf4j
public class CommunityController {
  private final CommunityService communityService;
  private final CommunityApiMapper communityApiMapper;

  @Operation(
      description = "Create a new community",
      responses = {
          @ApiResponse(responseCode = "201", description = "If community was created"),
      }
  )

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
  public ResponseEntity<GetCommunityDetailsResponse> listAllCommunity(
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all community");

    Set<Community> communityDetails = communityService.listAll(pageable);
    Set<GetCommunityDetailsResponse.Community> communityDetailsResponse =
        communityApiMapper.communitySetToRestApiResponseCommunitySet(communityDetails);

    GetCommunityDetailsResponse response = new GetCommunityDetailsResponse();
    response.getCommunities().addAll(communityDetailsResponse);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Operation(
      description = "Get details about the community given a community id",
      responses = {
          @ApiResponse(responseCode = "200", description = "If community exists"),
          @ApiResponse(responseCode = "404", description = "If params are invalid"),
      }
  )
  @GetMapping(
      path = "/communities/{communityId}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<GetCommunityDetailsResponse> listCommunityDetails(
      @PathVariable String communityId) {
    log.trace("Received request to get details about community with id[{}]", communityId);

    return communityService.getCommunityDetailsById(communityId)
        .map(communityApiMapper::communityToRestApiResponseCommunity)
        .map(Arrays::asList)
        .map(HashSet::new)
        .map(GetCommunityDetailsResponse::new)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(
      description = "List all admins of the community given a community id",
      responses = {
          @ApiResponse(responseCode = "200", description = "If community exists"),
          @ApiResponse(responseCode = "404", description = "If params are invalid"),
      }
  )
  @GetMapping(
      path = "/communities/{communityId}/admins",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<ListCommunityAdminsResponse> listCommunityAdmins(
      @PathVariable String communityId,
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all admins of community with id[{}]", communityId);

    return communityService.findCommunityAdminsById(communityId, pageable)
        .map(HashSet::new)
        .map(communityApiMapper::communityAdminSetToRestApiResponseCommunityAdminSet)
        .map(ListCommunityAdminsResponse::new)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(
      description = "List all houses of the community given a community id",
      responses = {
          @ApiResponse(responseCode = "200", description = "If community exists"),
          @ApiResponse(responseCode = "404", description = "If params are invalid"),
      }
  )
  @GetMapping(
      path = "/communities/{communityId}/houses",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<GetHouseDetailsResponse> listCommunityHouses(
      @PathVariable String communityId,
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all houses of community with id[{}]", communityId);

    return communityService.findCommunityHousesById(communityId, pageable)
        .map(HashSet::new)
        .map(communityApiMapper::communityHouseSetToRestApiResponseCommunityHouseSet)
        .map(GetHouseDetailsResponse::new)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(
      description = "Add a new admin to the community given a community id",
      responses = {
          @ApiResponse(responseCode = "204", description = "If admins were added"),
          @ApiResponse(responseCode = "404", description = "If params are invalid"),
      }
  )
  @PostMapping(
      path = "/communities/{communityId}/admins",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<AddCommunityAdminResponse> addCommunityAdmins(
      @PathVariable String communityId, @Valid @RequestBody
      AddCommunityAdminRequest request) {
    log.trace("Received request to add admin to community with id[{}]", communityId);
    Optional<Community> communityOptional =
        communityService.addAdminsToCommunity(communityId, request.getAdmins());
    return communityOptional.map(community -> {
      Set<String> adminsSet = community.getAdmins()
          .stream()
          .map(User::getUserId)
          .collect(Collectors.toSet());
      AddCommunityAdminResponse response = new AddCommunityAdminResponse(adminsSet);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Operation(
      description = "Add a new house to the community given a community id",
      responses = {
          @ApiResponse(responseCode = "204", description = "If houses were added"),
          @ApiResponse(responseCode = "400", description = "If params are invalid"),
      }
  )
  @PostMapping(
      path = "/communities/{communityId}/houses",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<AddCommunityHouseResponse> addCommunityHouses(
      @PathVariable String communityId, @Valid @RequestBody
      AddCommunityHouseRequest request) {
    log.trace("Received request to add house to community with id[{}]", communityId);
    Set<CommunityHouseName> houseNames = request.getHouses();
    Set<CommunityHouse> communityHouses =
        communityApiMapper.communityHouseNamesSetToCommunityHouseSet(houseNames);
    Set<String> houseIds = communityService.addHousesToCommunity(communityId, communityHouses);
    if (houseIds.size() != 0 && houseNames.size() != 0) {
      AddCommunityHouseResponse response = new AddCommunityHouseResponse();
      response.setHouses(houseIds);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @Operation(
      description = "Remove of house from the community given a community id and a house id",
      responses = {
          @ApiResponse(responseCode = "204", description = "If house was added"),
          @ApiResponse(responseCode = "400", description = "If params are invalid"),
      }
  )
  @DeleteMapping(
      path = "/communities/{communityId}/houses/{houseId}"
  )
  public ResponseEntity<Void> removeCommunityHouse(
      @PathVariable String communityId, @PathVariable String houseId
  ) {
    log.trace(
        "Received request to delete house with id[{}] from community with id[{}]",
        houseId, communityId);

    Optional<Community> communityOptional = communityService.getCommunityDetailsById(communityId);

    return communityOptional.filter(
        community -> communityService.removeHouseFromCommunityByHouseId(community, houseId))
        .map(removed -> ResponseEntity.noContent().<Void>build())
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(
      description = "Remove of admin associated with a community",
      responses = {
          @ApiResponse(responseCode = "204", description = "If admin was removed"),
          @ApiResponse(responseCode = "404", description = "If parameters are invalid")
      }
  )
  @DeleteMapping(
      path = "/communities/{communityId}/admins/{adminId}"
  )
  public ResponseEntity<Void> removeAdminFromCommunity(
      @PathVariable String communityId, @PathVariable String adminId) {
    log.trace(
        "Received request to delete an admin from community with community id[{}] and admin id[{}]",
        communityId, adminId);
    boolean adminRemoved = communityService.removeAdminFromCommunity(communityId, adminId);
    if (adminRemoved) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Operation(
      description = "Deletion community with given community id",
      responses = {
          @ApiResponse(responseCode = "204", description = "If community was removed"),
          @ApiResponse(responseCode = "404", description = "If parameters are invalid")
      }
  )
  @DeleteMapping(
      path = "/communities/{communityId}"
  )
  public ResponseEntity<Void> deleteCommunity(@PathVariable String communityId) {
    log.trace("Received delete community request");
    boolean isDeleted = communityService.deleteCommunity(communityId);
    if (isDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
