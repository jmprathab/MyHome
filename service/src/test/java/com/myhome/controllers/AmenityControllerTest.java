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
import com.myhome.controllers.mapper.AmenityBookingItemApiMapper;
import com.myhome.controllers.request.AddAmenityRequest;
import com.myhome.controllers.request.UpdateAmenityRequest;
import com.myhome.controllers.response.GetAmenityBookingsResponse;
import com.myhome.controllers.response.amenity.AddAmenityResponse;
import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import com.myhome.domain.Amenity;
import com.myhome.domain.AmenityBookingItem;
import com.myhome.services.AmenityService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class AmenityControllerTest {

  private static final String TEST_AMENITY_NAME = "test-amenity-name";
  private static final BigDecimal TEST_AMENITY_PRICE = BigDecimal.valueOf(1);
  private final String TEST_AMENITY_ID = "test-amenity-id";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";
  private final String TEST_COMMUNITY_ID = "1";

  @Mock
  private AmenityService amenitySDJpaService;
  @Mock
  private AmenityApiMapper amenityApiMapper;
  @Mock
  private AmenityBookingItemApiMapper amenityBookingItemApiMapper;

  @InjectMocks
  private AmenityController amenityController;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void getAmenityDetails() {
    // given
    Amenity testAmenity = getTestAmenity();
    GetAmenityDetailsResponse expectedResponseBody =
        new GetAmenityDetailsResponse(testAmenity.getAmenityId(), testAmenity.getDescription());

    given(amenitySDJpaService.getAmenityDetails(TEST_AMENITY_ID))
        .willReturn(Optional.of(testAmenity));
    given(amenityApiMapper.amenityToAmenityDetailsResponse(testAmenity))
        .willReturn(expectedResponseBody);

    // when
    ResponseEntity<GetAmenityDetailsResponse> response =
        amenityController.getAmenityDetails(TEST_AMENITY_ID);

    // then
    assertEquals(expectedResponseBody, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(amenitySDJpaService).getAmenityDetails(TEST_AMENITY_ID);
    verify(amenityApiMapper).amenityToAmenityDetailsResponse(testAmenity);
  }

  @Test
  void getAmenityDetailsNotExists() {
    // given
    given(amenitySDJpaService.getAmenityDetails(TEST_AMENITY_ID))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<GetAmenityDetailsResponse> response =
        amenityController.getAmenityDetails(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(amenitySDJpaService).getAmenityDetails(TEST_AMENITY_ID);
    verify(amenityApiMapper, never()).amenityToAmenityDetailsResponse(any());
  }

  @Test
  void deleteAmenity() {
    // given
    given(amenitySDJpaService.deleteAmenity(TEST_AMENITY_ID))
        .willReturn(true);

    // when
    ResponseEntity response = amenityController.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(amenitySDJpaService).deleteAmenity(TEST_AMENITY_ID);
  }

  @Test
  void deleteAmenityNotExists() {
    // given
    given(amenitySDJpaService.deleteAmenity(TEST_AMENITY_ID))
        .willReturn(false);

    // when
    ResponseEntity response = amenityController.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(amenitySDJpaService).deleteAmenity(TEST_AMENITY_ID);
  }

  @Test
  void shouldAddAmenityToCommunity() {
    // given
    final String communityId = "communityId";
    final AmenityDto amenityDto =
        new AmenityDto(1L, "amenityId", "name", "description", BigDecimal.ONE, "");
    final HashSet<AmenityDto> amenities = new HashSet<>(singletonList(amenityDto));
    final AddAmenityRequest request = new AddAmenityRequest(amenities);
    given(amenitySDJpaService.createAmenities(amenities, communityId))
        .willReturn(Optional.of(singletonList(amenityDto)));

    // when
    final ResponseEntity<AddAmenityResponse> response =
        amenityController.addAmenityToCommunity(request, communityId);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void shouldNotAddAmenityWhenCommunityNotExists() {
    // given
    final String communityId = "communityId";
    final AmenityDto amenityDto = new AmenityDto();
    final HashSet<AmenityDto> amenities = new HashSet<>(singletonList(amenityDto));
    final AddAmenityRequest request = new AddAmenityRequest(amenities);
    given(amenitySDJpaService.createAmenities(amenities, communityId))
        .willReturn(Optional.empty());

    // when
    final ResponseEntity<AddAmenityResponse> response =
        amenityController.addAmenityToCommunity(request, communityId);

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void shouldUpdateAmenitySuccessfully() {
    // given
    AmenityDto amenityDto = getTestAmenityDto();
    UpdateAmenityRequest request = getUpdateAmenityRequest();

    given(amenityApiMapper.updateAmenityRequestToAmenityDto(request))
        .willReturn(amenityDto);
    given(amenitySDJpaService.updateAmenity(amenityDto))
        .willReturn(true);

    // when
    ResponseEntity<Void> responseEntity =
        amenityController.updateAmenity(TEST_AMENITY_ID, request);

    // then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(amenityApiMapper).updateAmenityRequestToAmenityDto(request);
    verify(amenitySDJpaService).updateAmenity(amenityDto);
  }

  @Test
  void shouldNotUpdateCommunityAmenityIfAmenityNotExists() {
    // given
    AmenityDto amenityDto = getTestAmenityDto();
    UpdateAmenityRequest request = getUpdateAmenityRequest();

    given(amenityApiMapper.updateAmenityRequestToAmenityDto(request))
        .willReturn(amenityDto);
    given(amenitySDJpaService.updateAmenity(amenityDto))
        .willReturn(false);

    // when
    ResponseEntity<Void> responseEntity =
        amenityController.updateAmenity(TEST_AMENITY_ID, request);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(amenityApiMapper).updateAmenityRequestToAmenityDto(request);
    verify(amenitySDJpaService).updateAmenity(amenityDto);
  }

  @Test
  void shouldReturnListOfBookingsOfGivenAmenityId() {
    // given
    AmenityBookingItem amenityBookingItem = new AmenityBookingItem();
    amenityBookingItem.setAmenity(getTestAmenity());
    LocalDateTime currentDate = LocalDateTime.now();
    GetAmenityBookingsResponse getAmenityBookingsResponse = new GetAmenityBookingsResponse();
    getAmenityBookingsResponse.setAmenityId(TEST_AMENITY_ID);

    List<AmenityBookingItem> amenityBookingItems = Collections.singletonList(amenityBookingItem);
    given(amenitySDJpaService.listAllAmenityBookings(TEST_AMENITY_ID,
        currentDate,
        currentDate.plusDays(5),
        null))
        .willReturn(amenityBookingItems);
    given(amenityBookingItemApiMapper.amenityBookingToAmenityBookingsResponse(amenityBookingItems))
        .willReturn(Collections.singletonList(getAmenityBookingsResponse));

    // when
    ResponseEntity<List<GetAmenityBookingsResponse>> amenitiesBookings =
        amenityController.getAmenitiesBookings(TEST_AMENITY_ID,
            currentDate,
            currentDate.plusDays(5),
            null);

    // then
    assertEquals(HttpStatus.OK, amenitiesBookings.getStatusCode());
    assertNotNull(amenitiesBookings.getBody());
    assertEquals(1, amenitiesBookings.getBody().size());
  }

  @Test
  void shouldReturnEmptyListWhenNoBookingsFound() {
    // given
    LocalDateTime currentDate = LocalDateTime.now();
    given(amenitySDJpaService.listAllAmenityBookings(TEST_AMENITY_ID,
        currentDate,
        currentDate.plusDays(5),
        null))
        .willReturn(Collections.emptyList());

    // when
    ResponseEntity<List<GetAmenityBookingsResponse>> amenitiesBookings =
        amenityController.getAmenitiesBookings(TEST_AMENITY_ID,
            currentDate,
            currentDate.plusDays(5),
            null);

    // then
    assertEquals(HttpStatus.OK, amenitiesBookings.getStatusCode());
    assertNotNull(amenitiesBookings.getBody());
    assertEquals(0, amenitiesBookings.getBody().size());
  }

  @Test
  void shouldReturnBookingsWithoutDateRange() {
    // given
    AmenityBookingItem amenityBookingItem = new AmenityBookingItem();
    amenityBookingItem.setAmenity(getTestAmenity());
    GetAmenityBookingsResponse getAmenityBookingsResponse = new GetAmenityBookingsResponse();
    getAmenityBookingsResponse.setAmenityId(TEST_AMENITY_ID);

    List<AmenityBookingItem> amenityBookingItems = Collections.singletonList(amenityBookingItem);
    given(amenitySDJpaService.listAllAmenityBookings(TEST_AMENITY_ID,
        null,
        null,
        null))
        .willReturn(amenityBookingItems);
    given(amenityBookingItemApiMapper.amenityBookingToAmenityBookingsResponse(amenityBookingItems))
        .willReturn(Collections.singletonList(getAmenityBookingsResponse));

    // when
    ResponseEntity<List<GetAmenityBookingsResponse>> amenitiesBookings =
        amenityController.getAmenitiesBookings(TEST_AMENITY_ID,
            null,
            null,
            null);

    // then
    assertEquals(HttpStatus.OK, amenitiesBookings.getStatusCode());
    assertNotNull(amenitiesBookings.getBody());
    assertEquals(1, amenitiesBookings.getBody().size());
  }

  @Test
  void shouldReturnBookingsWithStartDateRange() {
    // given
    AmenityBookingItem amenityBookingItem = new AmenityBookingItem();
    amenityBookingItem.setAmenity(getTestAmenity());
    GetAmenityBookingsResponse getAmenityBookingsResponse = new GetAmenityBookingsResponse();
    getAmenityBookingsResponse.setAmenityId(TEST_AMENITY_ID);
    LocalDateTime currentTime = LocalDateTime.now();

    List<AmenityBookingItem> amenityBookingItems = Collections.singletonList(amenityBookingItem);
    given(amenitySDJpaService.listAllAmenityBookings(TEST_AMENITY_ID,
        currentTime,
        null,
        null))
        .willReturn(amenityBookingItems);
    given(amenityBookingItemApiMapper.amenityBookingToAmenityBookingsResponse(amenityBookingItems))
        .willReturn(Collections.singletonList(getAmenityBookingsResponse));

    // when
    ResponseEntity<List<GetAmenityBookingsResponse>> amenitiesBookings =
        amenityController.getAmenitiesBookings(TEST_AMENITY_ID,
            currentTime,
            null,
            null);

    // then
    assertEquals(HttpStatus.OK, amenitiesBookings.getStatusCode());
    assertNotNull(amenitiesBookings.getBody());
    assertEquals(1, amenitiesBookings.getBody().size());
  }

  @Test
  void shouldReturnBookingsWithEndDateRange() {
    // given
    AmenityBookingItem amenityBookingItem = new AmenityBookingItem();
    amenityBookingItem.setAmenity(getTestAmenity());
    GetAmenityBookingsResponse getAmenityBookingsResponse = new GetAmenityBookingsResponse();
    getAmenityBookingsResponse.setAmenityId(TEST_AMENITY_ID);
    LocalDateTime futureTime = LocalDateTime.now().plusWeeks(2);

    List<AmenityBookingItem> amenityBookingItems = Collections.singletonList(amenityBookingItem);
    given(amenitySDJpaService.listAllAmenityBookings(TEST_AMENITY_ID,
        null,
        futureTime,
        null))
        .willReturn(amenityBookingItems);
    given(amenityBookingItemApiMapper.amenityBookingToAmenityBookingsResponse(amenityBookingItems))
        .willReturn(Collections.singletonList(getAmenityBookingsResponse));

    // when
    ResponseEntity<List<GetAmenityBookingsResponse>> amenitiesBookings =
        amenityController.getAmenitiesBookings(TEST_AMENITY_ID,
            null,
            futureTime,
            null);

    // then
    assertEquals(HttpStatus.OK, amenitiesBookings.getStatusCode());
    assertNotNull(amenitiesBookings.getBody());
    assertEquals(1, amenitiesBookings.getBody().size());
  }

  private Amenity getTestAmenity() {
    return new Amenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION);
  }

  private AmenityDto getTestAmenityDto() {
    return new AmenityDto(
        Long.valueOf(1),
        TEST_AMENITY_ID,
        TEST_AMENITY_NAME,
        TEST_AMENITY_DESCRIPTION,
        TEST_AMENITY_PRICE,
        TEST_COMMUNITY_ID
    );
  }

  private UpdateAmenityRequest getUpdateAmenityRequest() {
    return new UpdateAmenityRequest(
        TEST_AMENITY_NAME,
        TEST_AMENITY_DESCRIPTION,
        1,
        TEST_COMMUNITY_ID
    );
  }
}