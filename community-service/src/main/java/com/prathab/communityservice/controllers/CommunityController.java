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

package com.prathab.communityservice.controllers;

import com.prathab.communityservice.controllers.models.mapper.CommunityApiMapper;
import com.prathab.communityservice.controllers.models.request.CreateCommunityRequest;
import com.prathab.communityservice.controllers.models.response.CreateCommunityResponse;
import com.prathab.communityservice.services.CommunityService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/communities/status")
  public String status() {
    return "Working";
  }

  @PostMapping(
      path = "/communities",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<CreateCommunityResponse> createCommunity(@Valid @RequestBody
      CreateCommunityRequest request) {
    log.trace("Received create community request");
    var requestCommunityDto = communityApiMapper.createCommunityRequestToCommunityDto(request);
    var createdCommunityDto = communityService.createCommunity(requestCommunityDto);
    var createdCommunityResponse =
        communityApiMapper.communityDtoToCreateCommunityResponse(createdCommunityDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCommunityResponse);
  }
}
