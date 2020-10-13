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

import com.myhome.api.CommunitiesApi;
import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.mapper.CommunityApiMapper;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.User;
import com.myhome.model.AddCommunityAdminRequest;
import com.myhome.model.AddCommunityAdminResponse;
import com.myhome.model.AddCommunityHouseRequest;
import com.myhome.model.AddCommunityHouseResponse;
import com.myhome.model.CommunityHouseName;
import com.myhome.model.CreateCommunityRequest;
import com.myhome.model.CreateCommunityResponse;
import com.myhome.model.GetCommunityDetailsResponse;
import com.myhome.model.GetCommunityDetailsResponseCommunity;
import com.myhome.model.GetHouseDetailsResponse;
import com.myhome.model.ListCommunityAdminsResponse;
import com.myhome.services.CommunityService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which provides endpoints for managing community
 */
@RequiredArgsConstructor
@RestController
@Slf4j
public class CommunityController implements CommunitiesApi {
  private final CommunityService communityService;
  private final CommunityApiMapper communityApiMapper;

  @Override
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

  @Override
  public ResponseEntity<GetCommunityDetailsResponse> listAllCommunity(
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all community");

    Set<Community> communityDetails = communityService.listAll(pageable);
    Set<GetCommunityDetailsResponseCommunity> communityDetailsResponse =
        communityApiMapper.communitySetToRestApiResponseCommunitySet(communityDetails);

    GetCommunityDetailsResponse response = new GetCommunityDetailsResponse();
    response.getCommunities().addAll(communityDetailsResponse);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @Override
  public ResponseEntity<GetCommunityDetailsResponse> listCommunityDetails(
      @PathVariable String communityId) {
    log.trace("Received request to get details about community with id[{}]", communityId);

    return communityService.getCommunityDetailsById(communityId)
        .map(communityApiMapper::communityToRestApiResponseCommunity)
        .map(Arrays::asList)
        .map(HashSet::new)
        .map(communities -> new GetCommunityDetailsResponse().communities(communities))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<ListCommunityAdminsResponse> listCommunityAdmins(
      @PathVariable String communityId,
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all admins of community with id[{}]", communityId);

    return communityService.findCommunityAdminsById(communityId, pageable)
        .map(HashSet::new)
        .map(communityApiMapper::communityAdminSetToRestApiResponseCommunityAdminSet)
        .map(admins -> new ListCommunityAdminsResponse().admins(admins))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<GetHouseDetailsResponse> listCommunityHouses(
      @PathVariable String communityId,
      @PageableDefault(size = 200) Pageable pageable) {
    log.trace("Received request to list all houses of community with id[{}]", communityId);

    return communityService.findCommunityHousesById(communityId, pageable)
        .map(HashSet::new)
        .map(communityApiMapper::communityHouseSetToRestApiResponseCommunityHouseSet)
        .map(houses -> new GetHouseDetailsResponse().houses(houses))
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Override
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
      AddCommunityAdminResponse response = new AddCommunityAdminResponse().admins(adminsSet);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Override
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

  @Override
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

  @Override
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

  @Override
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
