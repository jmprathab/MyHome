package com.myhome.services.unit;

import com.myhome.domain.Community;
import com.myhome.domain.Amenity;
import com.myhome.repositories.AmenityRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.services.springdatajpa.AmenitySDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import static org.mockito.Mockito.verify;

class AmenitySDJpaServiceTest {

  private final String TEST_AMENITY_ID = "test-amenity-id";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";

  private final String TEST_COMMUNITY_ID = "test-community-id";
  private final String TEST_COMMUNITY_NAME = "test-community-name";
  private final String TEST_COMMUNITY_DISTRICT = "test-community-name";

  @Mock
  private AmenityRepository amenityRepository;
  @Mock
  private CommunityRepository communityRepository;

  @InjectMocks
  private AmenitySDJpaService amenitySDJpaService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void deleteAmenity() {
    // given
    Amenity testAmenity = getTestAmenity();

    given(amenityRepository.findByAmenityIdWithCommunity(TEST_AMENITY_ID))
        .willReturn(Optional.of(testAmenity));

    // when
    boolean amenityDeleted = amenitySDJpaService.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertTrue(amenityDeleted);
    verify(amenityRepository).findByAmenityIdWithCommunity(TEST_AMENITY_ID);
    verify(amenityRepository).delete(testAmenity);
  }

  @Test
  void deleteAmenityNotExists() {
    // given
    given(amenityRepository.findByAmenityIdWithCommunity(TEST_AMENITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean amenityDeleted = amenitySDJpaService.deleteAmenity(TEST_AMENITY_ID);

    // then
    assertFalse(amenityDeleted);
    verify(amenityRepository).findByAmenityIdWithCommunity(TEST_AMENITY_ID);
    verify(amenityRepository, never()).delete(any());
  }

  @Test
  void listAllAmenities() {
    // given
    Amenity testAmenity = getTestAmenity();
    Set<Amenity> testAmenities = Collections.singleton(testAmenity);
    Community testCommunity = getTestCommunity();
    testCommunity.setAmenities(testAmenities);

    given(communityRepository.findByCommunityIdWithAmenities(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));

    // when
    Set<Amenity> resultAmenities = amenitySDJpaService.listAllAmenities(TEST_COMMUNITY_ID);

    // then
    assertEquals(testAmenities, resultAmenities);
    verify(communityRepository).findByCommunityIdWithAmenities(TEST_COMMUNITY_ID);
  }

  @Test
  void listAllAmenitiesNotExists() {
    // given
    given(communityRepository.findByCommunityIdWithAmenities(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    Set<Amenity> resultAmenities = amenitySDJpaService.listAllAmenities(TEST_COMMUNITY_ID);

    // then
    assertEquals(new HashSet<>(), resultAmenities);
    verify(communityRepository).findByCommunityIdWithAmenities(TEST_COMMUNITY_ID);
  }

  private Amenity getTestAmenity() {
    return new Amenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION)
        .withCommunity(getTestCommunity());
  }

  private Community getTestCommunity() {
    return new Community(
        new HashSet<>(),
        new HashSet<>(),
        TEST_COMMUNITY_NAME,
        TEST_COMMUNITY_ID,
        TEST_COMMUNITY_DISTRICT,
        new HashSet<>()
    );
  }

}