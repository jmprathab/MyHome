package com.myhome.controllers;

import com.myhome.controllers.mapper.AmenityApiMapper;
import com.myhome.controllers.mapper.AmenityBookingItemApiMapper;
import com.myhome.controllers.request.AddAmenityRequest;
import com.myhome.controllers.response.GetAmenityBookingsResponse;
import com.myhome.controllers.response.amenity.AddAmenityResponse;
import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import com.myhome.domain.Amenity;
import com.myhome.domain.AmenityBookingItem;
import com.myhome.services.AmenityService;
import com.myhome.services.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AmenityController {

  private final AmenityService amenitySDJpaService;
  private final AmenityApiMapper amenityApiMapper;
  private final CommunityService communityService;
  private final AmenityBookingItemApiMapper amenityBookingItemApiMapper;

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

  @Operation(description = "Get all bookings for an amenity")
  @GetMapping(path = "/amenities/{amenityId}/bookings")
  public ResponseEntity<List<GetAmenityBookingsResponse>> getAmenitiesBookings(
          @PathVariable String amenityId,
          @RequestParam(required = false) LocalDateTime start,
          @RequestParam(required = false) LocalDateTime end,
          @PageableDefault(size = 200) Pageable pageable) {

    List<AmenityBookingItem> amenityBookingItems =
            amenitySDJpaService.listAllAmenityBookings(amenityId, start, end, pageable);
    if (amenityBookingItems.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    List<GetAmenityBookingsResponse> response =
            amenityBookingItemApiMapper.amenityBookingToAmenityBookingsResponse(amenityBookingItems);
    return ResponseEntity.ok(response);
  }
}
