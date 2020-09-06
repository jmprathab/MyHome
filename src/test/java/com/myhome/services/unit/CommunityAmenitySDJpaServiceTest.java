package com.myhome.services.unit;

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

class CommunityAmenitySDJpaServiceTest {

  private final String TEST_AMENITY_ID = "test-amenity-id";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";

  private final String TEST_COMMUNITY_ID = "test-community-id";
  private final String TEST_COMMUNITY_NAME = "test-community-name";
  private final String TEST_COMMUNITY_DISTRICT = "test-community-name";

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

    given(communityRepository.findByCommunityIdWithAmenities(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));

    // when
    Set<CommunityAmenity> resultAmenities = communityAmenitySDJpaService.listAllCommunityAmenities(TEST_COMMUNITY_ID);

    // then
    assertEquals(testAmenities, resultAmenities);
    verify(communityRepository).findByCommunityIdWithAmenities(TEST_COMMUNITY_ID);
  }

  @Test
  void listAllCommunityAmenitiesNotExists() {
    // given
    given(communityRepository.findByCommunityIdWithAmenities(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    Set<CommunityAmenity> resultAmenities = communityAmenitySDJpaService.listAllCommunityAmenities(TEST_COMMUNITY_ID);

    // then
    assertEquals(new HashSet<>(), resultAmenities);
    verify(communityRepository).findByCommunityIdWithAmenities(TEST_COMMUNITY_ID);
  }

  private CommunityAmenity getTestAmenity() {
    return new CommunityAmenity()
        .withAmenityId(TEST_AMENITY_ID)
        .withDescription(TEST_AMENITY_DESCRIPTION)
        .withCommunity(getTestCommunity());
  }

  private Community getTestCommunity() {
    Community testCommunity = new Community(
        new HashSet<>(),
        new HashSet<>(),
        TEST_COMMUNITY_NAME,
        TEST_COMMUNITY_ID,
        TEST_COMMUNITY_DISTRICT,
        new HashSet<>()
    );
    return testCommunity;
  }

}