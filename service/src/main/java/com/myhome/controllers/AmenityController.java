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

import com.myhome.controllers.dto.AmenityDto;
import com.myhome.controllers.mapper.AmenityApiMapper;
import com.myhome.controllers.request.AddAmenityRequest;
import com.myhome.controllers.request.UpdateAmenityRequest;
import com.myhome.controllers.response.amenity.AddAmenityResponse;
import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import com.myhome.domain.Amenity;
import com.myhome.services.AmenityService;
import com.myhome.services.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AmenityController {

  private final AmenityService amenitySDJpaService;
  private final AmenityApiMapper amenityApiMapper;
  private final CommunityService communityService;

  @Operation(
      description = "Get details about the amenity",
      responses = {
          @ApiResponse(responseCode = "404", description = "If params are invalid"),
      }
  )
  @GetMapping("/amenities/{amenityId}")
  public ResponseEntity<GetAmenityDetailsResponse> getAmenityDetails(
      @PathVariable String amenityId) {
    return amenitySDJpaService.getAmenityDetails(amenityId)
        .map(amenityApiMapper::amenityToAmenityDetailsResponse)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Operation(
      description = "Remove amenity",
      responses = {
          @ApiResponse(responseCode = "204", description = "If amenity deleted"),
          @ApiResponse(responseCode = "404", description = "If params are invalid"),
      }
  )
  @DeleteMapping(path = "/amenities/{amenityId}")
  public ResponseEntity deleteAmenity(@PathVariable String amenityId) {
    boolean isAmenityDeleted = amenitySDJpaService.deleteAmenity(amenityId);
    if (isAmenityDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Operation(description = "Get all amenities of community")
  @GetMapping(
      path = "/communities/{communityId}/amenities",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<Set<GetAmenityDetailsResponse>> listAllAmenities(
      @PathVariable String communityId) {
    Set<Amenity> amenities = amenitySDJpaService.listAllAmenities(communityId);
    Set<GetAmenityDetailsResponse> response =
        amenityApiMapper.amenitiesSetToAmenityDetailsResponseSet(amenities);
    return ResponseEntity.ok(response);
  }

  @Operation(description = "Adds amenity to community")
  @PostMapping(
      path = "/communities/{communityId}/amenities",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<AddAmenityResponse> addAmenityToCommunity(
      @RequestBody AddAmenityRequest request,
      @PathVariable String communityId) {
    return amenitySDJpaService.createAmenities(request.getAmenities(), communityId)
        .map(AddAmenityResponse::new)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(
      description = "Update an amenity",
      responses = {
          @ApiResponse(responseCode = "204", description = "If updated successfully"),
          @ApiResponse(responseCode = "400", description = "If amenity is not found"),
      }
  )
  @PutMapping(path = "amenities/{amenityId}")
  public ResponseEntity<Void> updateAmenity(@PathVariable String amenityId,
      @Valid @RequestBody UpdateAmenityRequest request) {
    AmenityDto amenityDto = amenityApiMapper.updateAmenityRequestToAmenityDto(request);
    amenityDto.setAmenityId(amenityId);
    boolean isUpdated = amenitySDJpaService.updateAmenity(amenityDto);
    if (isUpdated) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
