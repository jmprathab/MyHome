package com.myhome.controllers;

import com.myhome.controllers.mapper.CommunityAmenityApiMapper;
import com.myhome.controllers.response.amenity.GetCommunityAmenityDetailsResponse;
import com.myhome.domain.CommunityAmenity;
import com.myhome.services.CommunityAmenityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommunityAmenityController {

  private final CommunityAmenityService communityAmenitySDJpaService;
  private final CommunityAmenityApiMapper communityAmenityApiMapper;

  @Operation(
      description = "Get details about the amenity",
      responses = {
          @ApiResponse(responseCode = "404", description = "If params are invalid"),
      }
  )
  @GetMapping("/amenities/{amenityId}")
  public ResponseEntity getAmenityDetails(@PathVariable String amenityId) {
    Optional<CommunityAmenity> communityAmenityOptional = communityAmenitySDJpaService.getCommunityAmenityDetails(amenityId);
    return communityAmenityOptional
        .map(communityAmenity -> {
          GetCommunityAmenityDetailsResponse response = communityAmenityApiMapper
              .communityAmenityToCommunityAmenityDetailsResponse(communityAmenity);
          return ResponseEntity.status(HttpStatus.OK).body(response);
        })
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }


  @GetMapping("/communities/{communityId}/amenities}")
  public ResponseEntity listAllCommunityAmenities(@PathVariable String amenityId) {
    Optional<CommunityAmenity> communityAmenityOptional = communityAmenitySDJpaService.getCommunityAmenityDetails(amenityId);
    return communityAmenityOptional
        .map(communityAmenity -> {
          GetCommunityAmenityDetailsResponse response = communityAmenityApiMapper
              .communityAmenityToCommunityAmenityDetailsResponse(communityAmenity);
          return ResponseEntity.status(HttpStatus.OK).body(response);
        })
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
    boolean isAmenityDeleted = communityAmenitySDJpaService.deleteAmenity(amenityId);
    if(isAmenityDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

}
