package com.myhome.services.unit;

import com.myhome.controllers.dto.CommunityAmenityDto;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityAmenity;
import com.myhome.repositories.CommunityAmenityRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.services.springdatajpa.CommunityAmenitySDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class CommunityAmenitySDJpaServiceTest {

  private final String TEST_AMENITY_ID = "test-amenity-id";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";

  private final String TEST_COMMUNITY_ID = "test-community-id";

  @Mock
  private CommunityAmenityRepository communityAmenityRepository;
  @Mock
  private CommunityRepository communityRepository;

  @InjectMocks
  private CommunityAmenitySDJpaService communityAmenitySDJpaService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void deleteAmenity() {
    // given
    CommunityAmenity testAmenity = getTestAmenity();

    given(communityAmenityRepository.findByAmenityIdWithCommunity(TEST_AMENITY_ID))
        .willReturn(Optional.of(testAmenity));

    // when
    boolean amenityDeleted = communityAmenitySDJpaService.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertTrue(amenityDeleted);
    verify(communityAmenityRepository).findByAmenityIdWithCommunity(TEST_AMENITY_ID);
    verify(communityAmenityRepository).delete(testAmenity);
  }

  @Test
  void deleteAmenityNotExists() {
    // given
    given(communityAmenityRepository.findByAmenityIdWithCommunity(TEST_AMENITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean amenityDeleted = communityAmenitySDJpaService.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertFalse(amenityDeleted);
    verify(communityAmenityRepository).findByAmenityIdWithCommunity(TEST_AMENITY_ID);
    verify(communityAmenityRepository, never()).delete(any());
  }

  @Test
  void listAllCommunityAmenities() {
    // given
    CommunityAmenity testCommunityAmenity = getTestAmenity();
    Set<CommunityAmenity> testAmenities = Collections.singleton(testCommunityAmenity);
    Community testCommunity = getTestCommunity();
    testCommunity.setAmenities(testAmenities);

    given(communityRepository.findByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));

    // when
    Set<CommunityAmenity> resultAmenities = communityAmenitySDJpaService.listAllCommunityAmenities(TEST_COMMUNITY_ID);

    // then
    assertEquals(testAmenities, resultAmenities);
    verify(communityRepository).findByCommunityId(TEST_COMMUNITY_ID);
  }

  @Test
  void listAllCommunityAmenitiesNotExists() {
    // given
    given(communityRepository.findByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    Set<CommunityAmenity> resultAmenities = communityAmenitySDJpaService.listAllCommunityAmenities(TEST_COMMUNITY_ID);

    // then
    assertEquals(new HashSet<>(), resultAmenities);
    verify(communityRepository).findByCommunityId(TEST_COMMUNITY_ID);
  }

  @Test
  void shouldUpdateCommunityAmenitySuccessfully() {
    // given
    CommunityAmenity communityAmenity = getTestAmenity();
    Community testCommunity = getTestCommunity();
    CommunityAmenityDto updated = getTestAmenityDto();
    CommunityAmenity updatedAmenity = getUpdatedCommunityAmenity();

    given(communityAmenityRepository.findByAmenityId(TEST_AMENITY_ID))
      .willReturn(Optional.of(communityAmenity));
    given(communityRepository.findByCommunityId(TEST_COMMUNITY_ID))
      .willReturn(Optional.of(testCommunity));
    given(communityAmenityRepository.save(updatedAmenity))
      .willReturn(updatedAmenity);

    // when
    boolean result = communityAmenitySDJpaService.updateAmenity(updated);

    // then
    assertTrue(result);
    verify(communityAmenityRepository).findByAmenityId(TEST_AMENITY_ID);
    verify(communityRepository).findByCommunityId(TEST_COMMUNITY_ID);
    verify(communityAmenityRepository).save(updatedAmenity);
  }

  @Test
  void shouldNotUpdateCommunityAmenitySuccessfullyIfAmenityNotExists() {
    // given
      given(communityAmenityRepository.findByAmenityId(TEST_AMENITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean result = communityAmenitySDJpaService.updateAmenity(getTestAmenityDto());

    // then
    assertFalse(result);
    verify(communityAmenityRepository, times(0)).save(getUpdatedCommunityAmenity());
    verifyNoInteractions(communityRepository);
  }

  @Test
  void shouldNotUpdateCommunityAmenitySuccessfullyIfSavingFails() {
    // given
    CommunityAmenity testAmenity = getTestAmenity();
    CommunityAmenity updatedAmenity = getUpdatedCommunityAmenity();
    CommunityAmenityDto updatedDto = getTestAmenityDto();
    Community community = getTestCommunity();

    given(communityAmenityRepository.findByAmenityId(TEST_AMENITY_ID))
      .willReturn(Optional.of(testAmenity));
    given(communityRepository.findByCommunityId(TEST_COMMUNITY_ID))
      .willReturn(Optional.of(community));
    given(communityAmenityRepository.save(updatedAmenity))
      .willReturn(null);

    // when
    boolean result = communityAmenitySDJpaService.updateAmenity(updatedDto);

    // then
    assertFalse(result);
    verify(communityAmenityRepository).findByAmenityId(TEST_AMENITY_ID);
    verify(communityRepository).findByCommunityId(TEST_COMMUNITY_ID);
    verify(communityAmenityRepository).save(updatedAmenity);
  }

  @Test
  void shouldNotUpdateAmenityIfCommunityDoesNotExist() {
    // given
    CommunityAmenity communityAmenity = getTestAmenity();
    CommunityAmenityDto updatedDto = getTestAmenityDto();

    given(communityAmenityRepository.findByAmenityId(TEST_AMENITY_ID))
      .willReturn(Optional.of(communityAmenity));
    given(communityRepository.findByCommunityId(TEST_COMMUNITY_ID))
      .willReturn(Optional.empty());

    // when
    boolean result = communityAmenitySDJpaService.updateAmenity(updatedDto);

    // then
    assertFalse(result);
    verify(communityAmenityRepository).findByAmenityId(TEST_AMENITY_ID);
    verify(communityRepository).findByCommunityId(TEST_COMMUNITY_ID);
    verifyNoMoreInteractions(communityAmenityRepository);
  }

  private CommunityAmenity getTestAmenity() {
    return new CommunityAmenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION)
        .withCommunity(getTestCommunity());
  }

  private Community getTestCommunity() {
    String TEST_COMMUNITY_DISTRICT = "test-community-name";
    String TEST_COMMUNITY_NAME = "test-community-name";
    return new Community(
        new HashSet<>(),
        new HashSet<>(),
    TEST_COMMUNITY_NAME,
        TEST_COMMUNITY_ID,
    TEST_COMMUNITY_DISTRICT,
        new HashSet<>()
    );
  }

  private CommunityAmenityDto getTestAmenityDto() {
    String TEST_AMENITY_START_DATE = "2020-09-01 19:00:30";
    String TEST_AMENITY_END_DATE = "2020-09-20 19:00:00";
    boolean TEST_AMENITY_IS_BOOKED = false;
    Long TEST_AMENITY_ENTITY_ID = 1L;

    return new CommunityAmenityDto(
      TEST_AMENITY_ENTITY_ID,
      TEST_AMENITY_ID,
      TEST_AMENITY_DESCRIPTION,
      TEST_AMENITY_IS_BOOKED,
      TEST_AMENITY_START_DATE,
      TEST_AMENITY_END_DATE,
      TEST_COMMUNITY_ID
    );
  }

  private CommunityAmenity getUpdatedCommunityAmenity() {
    CommunityAmenityDto communityAmenityDto = getTestAmenityDto();
    return new CommunityAmenity()
      .withAmenityId(communityAmenityDto.getAmenityId())
      .withBooked(communityAmenityDto.isBooked())
      .withBookingStartDate(LocalDateTime.parse(communityAmenityDto.getBookingStartDate(),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
      .withBookingEndDate(LocalDateTime.parse(communityAmenityDto.getBookingEndDate(),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
      .withDescription(communityAmenityDto.getDescription())
      .withCommunity(getTestCommunity());
  }
}