package com.myhome.controllers.unit;

import com.myhome.controllers.CommunityAmenityController;
import com.myhome.controllers.mapper.CommunityAmenityApiMapper;
import com.myhome.controllers.response.amenity.GetCommunityAmenityDetailsResponse;
import com.myhome.domain.CommunityAmenity;
import com.myhome.services.CommunityAmenityService;
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

class CommunityAmenityControllerTest {

  private final String TEST_AMENITY_ID = "test-amenity-id";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";

  @Mock
  private CommunityAmenityService communityAmenitySDJpaService;
  @Mock
  private CommunityAmenityApiMapper communityAmenityApiMapper;

  @InjectMocks
  private CommunityAmenityController communityAmenityController;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void getAmenityDetails() {
    // given
    CommunityAmenity testAmenity = getTestAmenity();
    GetCommunityAmenityDetailsResponse expectedResponseBody = new GetCommunityAmenityDetailsResponse(testAmenity.getAmenityId(), testAmenity.getDescription());

    given(communityAmenitySDJpaService.getCommunityAmenityDetails(TEST_AMENITY_ID))
        .willReturn(Optional.of(testAmenity));
    given(communityAmenityApiMapper.communityAmenityToCommunityAmenityDetailsResponse(testAmenity))
        .willReturn(expectedResponseBody);

    // when
    ResponseEntity<GetCommunityAmenityDetailsResponse> response = communityAmenityController.getAmenityDetails(TEST_AMENITY_ID);

    // then
    assertEquals(expectedResponseBody, response.getBody());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(communityAmenitySDJpaService).getCommunityAmenityDetails(TEST_AMENITY_ID);
    verify(communityAmenityApiMapper).communityAmenityToCommunityAmenityDetailsResponse(testAmenity);
  }

  @Test
  void getAmenityDetailsNotExists() {
    // given
    given(communityAmenitySDJpaService.getCommunityAmenityDetails(TEST_AMENITY_ID))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<GetCommunityAmenityDetailsResponse> response = communityAmenityController.getAmenityDetails(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(communityAmenitySDJpaService).getCommunityAmenityDetails(TEST_AMENITY_ID);
    verify(communityAmenityApiMapper, never()).communityAmenityToCommunityAmenityDetailsResponse(any());
  }

  @Test
  void deleteAmenity() {
    // given
    given(communityAmenitySDJpaService.deleteAmenity(TEST_AMENITY_ID))
        .willReturn(true);

    // when
    ResponseEntity response = communityAmenityController.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(communityAmenitySDJpaService).deleteAmenity(TEST_AMENITY_ID);
  }

  @Test
  void deleteAmenityNotExists() {
    // given
    given(communityAmenitySDJpaService.deleteAmenity(TEST_AMENITY_ID))
        .willReturn(false);

    // when
    ResponseEntity response = communityAmenityController.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertNull(response.getBody());
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(communityAmenitySDJpaService).deleteAmenity(TEST_AMENITY_ID);
  }

  private CommunityAmenity getTestAmenity() {
    return new CommunityAmenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION);
  }

}