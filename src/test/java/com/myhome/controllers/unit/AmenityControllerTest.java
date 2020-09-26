package com.myhome.controllers.unit;

import com.myhome.controllers.AmenityController;
import com.myhome.controllers.mapper.AmenityApiMapper;
import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import com.myhome.domain.Amenity;
import com.myhome.services.AmenityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class AmenityControllerTest {

  private final String TEST_AMENITY_ID = "test-amenity-id";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";

  @Mock
  private AmenityService amenitySDJpaService;
  @Mock
  private AmenityApiMapper amenityApiMapper;

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
    GetAmenityDetailsResponse expectedResponseBody = new GetAmenityDetailsResponse(testAmenity.getAmenityId(), testAmenity.getDescription());

    given(amenitySDJpaService.getAmenityDetails(TEST_AMENITY_ID))
        .willReturn(Optional.of(testAmenity));
    given(amenityApiMapper.amenityToAmenityDetailsResponse(testAmenity))
        .willReturn(expectedResponseBody);

    // when
    ResponseEntity<GetAmenityDetailsResponse> response = amenityController.getAmenityDetails(TEST_AMENITY_ID);

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
    ResponseEntity<GetAmenityDetailsResponse> response = amenityController.getAmenityDetails(TEST_AMENITY_ID);

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

  private Amenity getTestAmenity() {
    return new Amenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION);
  }

}