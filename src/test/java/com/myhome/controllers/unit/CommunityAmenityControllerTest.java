package com.myhome.controllers.unit;

import com.myhome.controllers.CommunityAmenityController;
import com.myhome.controllers.dto.CommunityAmenityDto;
import com.myhome.controllers.mapper.CommunityAmenityApiMapper;
import com.myhome.controllers.request.UpdateCommunityAmenityRequest;
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

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CommunityAmenityControllerTest {

  private final String TEST_AMENITY_ID = "test-amenity-id";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";
  private final String TEST_AMENITY_START_DATE = "2020-09-01 19:00:30";
  private final String TEST_AMENITY_END_DATE = "2020-09-20 19:00:00";
  private final String TEST_COMMUNITY_ID = "1";
  private final boolean TEST_AMENITY_IS_BOOKED = false;

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

  @Test
  void shouldUpdateAmenitySuccessfully() {
    // given
    CommunityAmenityDto communityAmenityDto = getTestAmenityDto();
    UpdateCommunityAmenityRequest request = getUpdateAmenityRequest();

    given(communityAmenityApiMapper.updateCommunityAmenityRequestToAmenityDto(request))
      .willReturn(communityAmenityDto);
    given(communityAmenitySDJpaService.updateAmenity(communityAmenityDto))
      .willReturn(true);

    // when
    ResponseEntity<Void> responseEntity =
      communityAmenityController.updateAmenity(TEST_AMENITY_ID, request);

    // then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(communityAmenityApiMapper).updateCommunityAmenityRequestToAmenityDto(request);
    verify(communityAmenitySDJpaService).updateAmenity(communityAmenityDto);
  }

  @Test
  void shouldNotUpdateCommunityAmenityIfAmenityNotExists() {
    // given
    CommunityAmenityDto communityAmenityDto = getTestAmenityDto();
    UpdateCommunityAmenityRequest request = getUpdateAmenityRequest();

    given(communityAmenityApiMapper.updateCommunityAmenityRequestToAmenityDto(request))
      .willReturn(communityAmenityDto);
    given(communityAmenitySDJpaService.updateAmenity(communityAmenityDto))
      .willReturn(false);

    // when
    ResponseEntity<Void> responseEntity =
      communityAmenityController.updateAmenity(TEST_AMENITY_ID, request);

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(communityAmenityApiMapper).updateCommunityAmenityRequestToAmenityDto(request);
    verify(communityAmenitySDJpaService).updateAmenity(communityAmenityDto);
  }

  @Test
  public void shouldListAllAmenitiesSuccessfully() {
    // given
    Set<CommunityAmenity> amenities = Collections.singleton(getTestAmenity());
    GetCommunityAmenityDetailsResponse response = new GetCommunityAmenityDetailsResponse(
      TEST_AMENITY_ID,
      TEST_AMENITY_DESCRIPTION
    );

    Set<GetCommunityAmenityDetailsResponse> responseSet = Collections.singleton(response);

    given(communityAmenitySDJpaService.listAllCommunityAmenities(TEST_COMMUNITY_ID))
      .willReturn(amenities);
    given(communityAmenityApiMapper.communityAmenitiesSetToCommunityAmenityDetailsResponseSet(amenities))
      .willReturn(responseSet);

    // when
    ResponseEntity<Set<GetCommunityAmenityDetailsResponse>> responseEntity =
      communityAmenityController.listAllCommunityAmenities(TEST_COMMUNITY_ID);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(responseEntity.getBody(), Collections.singleton(response));
    verify(communityAmenitySDJpaService).listAllCommunityAmenities(TEST_COMMUNITY_ID);
    verify(communityAmenityApiMapper).communityAmenitiesSetToCommunityAmenityDetailsResponseSet(amenities);
  }

  private CommunityAmenity getTestAmenity() {
    return new CommunityAmenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION);
  }

  private CommunityAmenityDto getTestAmenityDto() {
    return new CommunityAmenityDto(
      Long.valueOf(1),
      TEST_AMENITY_ID,
      TEST_AMENITY_DESCRIPTION,
      TEST_AMENITY_IS_BOOKED,
      TEST_AMENITY_START_DATE,
      TEST_AMENITY_START_DATE,
      TEST_COMMUNITY_ID
    );
  }

  private UpdateCommunityAmenityRequest getUpdateAmenityRequest() {
    return new UpdateCommunityAmenityRequest(
      TEST_AMENITY_DESCRIPTION,
      TEST_AMENITY_IS_BOOKED,
      TEST_AMENITY_START_DATE,
      TEST_AMENITY_END_DATE,
      TEST_COMMUNITY_ID
    );
  }

}