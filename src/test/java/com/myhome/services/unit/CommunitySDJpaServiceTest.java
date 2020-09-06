package com.myhome.services.unit;

import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.dto.mapper.CommunityMapper;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.User;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.springdatajpa.CommunitySDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CommunitySDJpaServiceTest {

  private final String TEST_COMMUNITY_ID = "test-community-id";
  private final String TEST_COMMUNITY_NAME = "test-community-name";
  private final String TEST_COMMUNITY_DISTRICT = "test-community-name";

  private final int TEST_ADMINS_COUNT = 2;
  private final int TEST_HOUSES_COUNT = 2;

  private final String TEST_ADMIN_ID = "test-admin-id";
  private final String TEST_ADMIN_NAME = "test-user-name";
  private final String TEST_ADMIN_EMAIL = "test-user-email";
  private final String TEST_ADMIN_PASSWORD = "test-user-password";

  private final String TEST_HOUSE_ID = "test-house-id";

  @Mock
  private CommunityRepository communityRepository;
  @Mock
  private UserRepository communityAdminRepository;
  @Mock
  private CommunityMapper communityMapper;
  @Mock
  private CommunityHouseRepository communityHouseRepository;

  @InjectMocks
  private CommunitySDJpaService communitySDJpaService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createCommunity() {
    // given
    CommunityDto testCommunityDto = getTestCommunityDto();
    Community testCommunity = getTestCommunity();

    given(communityMapper.communityDtoToCommunity(testCommunityDto))
        .willReturn(testCommunity);
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);

    // when
    Community createdCommunity = communitySDJpaService.createCommunity(testCommunityDto);

    // then
    assertNotNull(createdCommunity);
    assertEquals(testCommunityDto.getName(), createdCommunity.getName());
    assertEquals(testCommunityDto.getDistrict(), createdCommunity.getDistrict());
    verify(communityMapper).communityDtoToCommunity(testCommunityDto);
    verify(communityRepository).save(testCommunity);
  }

  @Test
  void findCommunityHousesById() {
    // given
    Community testCommunity = getTestCommunity();
    List<CommunityHouse> testCommunityHouses = new ArrayList<>(testCommunity.getHouses());
    given(communityRepository.existsByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(true);
    given(communityHouseRepository.findAllByCommunity_CommunityId(TEST_COMMUNITY_ID, null))
        .willReturn(testCommunityHouses);

    // when
    Optional<List<CommunityHouse>> resultCommunityHousesOptional = communitySDJpaService.findCommunityHousesById(TEST_COMMUNITY_ID, null);

    // then
    assertTrue(resultCommunityHousesOptional.isPresent());
    List<CommunityHouse> resultCommunityHouses = resultCommunityHousesOptional.get();
    assertEquals(testCommunityHouses, resultCommunityHouses);
    verify(communityRepository).existsByCommunityId(TEST_COMMUNITY_ID);
    verify(communityHouseRepository).findAllByCommunity_CommunityId(TEST_COMMUNITY_ID, null);
  }

  @Test
  void findCommunityHousesByIdNotExist() {
    // given
    given(communityRepository.existsByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(false);

    // when
    Optional<List<CommunityHouse>> resultCommunityHousesOptional = communitySDJpaService.findCommunityHousesById(TEST_COMMUNITY_ID, null);

    // then
    assertFalse(resultCommunityHousesOptional.isPresent());
    verify(communityRepository).existsByCommunityId(TEST_COMMUNITY_ID);
    verify(communityHouseRepository, never()).findAllByCommunity_CommunityId(TEST_COMMUNITY_ID, null);
  }

  @Test
  void findCommunityAdminsById() {
    // given
    Community testCommunity = getTestCommunity();
    List<User> testCommunityAdmins = new ArrayList<>(testCommunity.getAdmins());
    given(communityRepository.existsByCommunityId(TEST_COMMUNITY_ID))
        .willReturn(true);
    given(communityAdminRepository.findAllByCommunities_CommunityId(TEST_COMMUNITY_ID, null))
        .willReturn(testCommunityAdmins);

    // when
    Optional<List<User>> resultAdminsOptional = communitySDJpaService.findCommunityAdminsById(TEST_COMMUNITY_ID, null);

    // then
    assertTrue((resultAdminsOptional.isPresent()));
    List<User> resultAdmins = resultAdminsOptional.get();
    assertEquals(testCommunityAdmins, resultAdmins);
    verify(communityRepository).existsByCommunityId(TEST_COMMUNITY_ID);
    verify(communityAdminRepository).findAllByCommunities_CommunityId(TEST_COMMUNITY_ID, null);
  }

  @Test
  void addAdminsToCommunity() {
    // given
    Community testCommunity = getTestCommunity();
    Set<User> adminToAdd = getTestCommunityAdmins(TEST_ADMINS_COUNT);
    Set<String> adminToAddIds = adminToAdd.stream()
        .map(admin -> admin.getUserId())
        .collect(Collectors.toSet());

    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);
    adminToAdd.forEach(admin -> {
      given(communityAdminRepository.findByUserId(admin.getUserId()))
          .willReturn(Optional.of(admin));
    });
    adminToAdd.forEach(admin -> {
      given(communityAdminRepository.save(admin))
          .willReturn(admin);
    });
    // when
    Optional<Community> updatedCommunityOptional = communitySDJpaService.addAdminsToCommunity(TEST_COMMUNITY_ID, adminToAddIds);

    // then
    assertTrue(updatedCommunityOptional.isPresent());
    adminToAdd.forEach(admin -> assertTrue(admin.getCommunities().contains(testCommunity)));
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
    adminToAdd.forEach(admin -> verify(communityAdminRepository).findByUserIdWithCommunities(admin.getUserId()));
  }

  @Test
  void addAdminsToCommunityNotExist() {
    // given
    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    Optional<Community> updatedCommunityOptional = communitySDJpaService.addAdminsToCommunity(TEST_COMMUNITY_ID, any());

    // then
    assertFalse(updatedCommunityOptional.isPresent());
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
  }

  @Test
  void addHousesToCommunity() {
    // given
    Community testCommunity = getTestCommunity();
    Set<CommunityHouse> housesToAdd = getTestHouses(TEST_HOUSES_COUNT);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);
    housesToAdd.forEach(house -> {
      given(communityHouseRepository.save(house))
          .willReturn(house);
    });

    // when
    Set<String> addedHousesIds = communitySDJpaService.addHousesToCommunity(TEST_COMMUNITY_ID, housesToAdd);

    // then
    assertEquals(housesToAdd.size(), addedHousesIds.size());
    housesToAdd.forEach(house -> {
      assertEquals(house.getCommunity(), testCommunity);
    });
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    housesToAdd.forEach(house -> {
      verify(communityHouseRepository).save(house);
    });
  }

  @Test
  void addHousesToCommunityNotExist() {
    // given
    Set<CommunityHouse> housesToAdd = getTestHouses(TEST_HOUSES_COUNT);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    Set<String> addedHousesIds = communitySDJpaService.addHousesToCommunity(TEST_COMMUNITY_ID, housesToAdd);

    // then
    assertTrue(addedHousesIds.isEmpty());
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityRepository, never()).save(any());
    verify(communityHouseRepository, never()).save(any());
  }

  @Test
  void addHousesToCommunityHouseExists() {
    // given
    Community testCommunity = getTestCommunity();
    Set<CommunityHouse> houses = getTestHouses(TEST_HOUSES_COUNT);
    testCommunity.setHouses(houses);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);
    houses.forEach(house -> {
      given(communityHouseRepository.save(house))
          .willReturn(house);
    });

    // when
    Set<String> addedHousesIds = communitySDJpaService.addHousesToCommunity(TEST_COMMUNITY_ID, houses);

    // then
    assertTrue(addedHousesIds.isEmpty());
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityRepository).save(testCommunity);
    verify(communityHouseRepository, never()).save(any());
  }

  @Test
  void removeAdminFromCommunity() {
    // given
    Community testCommunity = getTestCommunity();
    User testAdmin = getTestAdmin();
    testCommunity.getAdmins().add(testAdmin);

    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);

    // when
    boolean adminRemoved = communitySDJpaService.removeAdminFromCommunity(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    // then
    assertTrue(adminRemoved);
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
    verify(communityRepository).save(testCommunity);
  }

  @Test
  void removeAdminFromCommunityNotExists() {
    // given
    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean adminRemoved = communitySDJpaService.removeAdminFromCommunity(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    // then
    assertFalse(adminRemoved);
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
    verify(communityRepository, never()).save(any());
  }

  @Test
  void removeAdminFromCommunityAdminNotExists() {
    // given
    Community testCommunity = getTestCommunity();

    given(communityRepository.findByCommunityIdWithAdmins(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityRepository.save(testCommunity))
        .willReturn(testCommunity);

    // when
    boolean adminRemoved = communitySDJpaService.removeAdminFromCommunity(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    // then
    assertFalse(adminRemoved);
    verify(communityRepository).findByCommunityIdWithAdmins(TEST_COMMUNITY_ID);
    verify(communityRepository, never()).save(testCommunity);
  }

  @Test
  void deleteCommunity() {
    // given
    Community testCommunity = getTestCommunity();
    Set<CommunityHouse> testCommunityHouses = getTestHouses(TEST_HOUSES_COUNT);
    testCommunity.setHouses(testCommunityHouses);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));

    // when
    boolean communityDeleted = communitySDJpaService.deleteCommunity(TEST_COMMUNITY_ID);

    // then
    assertTrue(communityDeleted);
    verify(communityRepository, times(TEST_HOUSES_COUNT + 1)).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityRepository).delete(testCommunity);
  }

  @Test
  void deleteCommunityNotExists() {
    // given
    Community testCommunity = getTestCommunity();

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean communityDeleted = communitySDJpaService.deleteCommunity(TEST_COMMUNITY_ID);

    // then
    assertFalse(communityDeleted);
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityHouseRepository, never()).deleteByHouseId(any());
    verify(communityRepository, never()).delete(testCommunity);
  }

  @Test
  void removeHouseFromCommunityByHouseId() {
    // given
    Community testCommunity = getTestCommunity();
    CommunityHouse testHouse = getTestCommunityHouse();
    testCommunity.getHouses().add(testHouse);

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityHouseRepository.findByHouseIdWithHouseMembers(TEST_HOUSE_ID))
        .willReturn(Optional.of(testHouse));

    // when
    boolean houseDeleted = communitySDJpaService.removeHouseFromCommunityByHouseId(TEST_COMMUNITY_ID, TEST_HOUSE_ID);

    // then
    assertTrue(houseDeleted);
    assertFalse(testCommunity.getHouses().contains(testHouse));
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityRepository).save(testCommunity);
    verify(communityHouseRepository).findByHouseIdWithHouseMembers(TEST_HOUSE_ID);
  }

  @Test
  void removeHouseFromCommunityByHouseIdCommunityNotExists() {
    // given
    Community testCommunity = getTestCommunity();

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    // when
    boolean houseDeleted = communitySDJpaService.removeHouseFromCommunityByHouseId(TEST_COMMUNITY_ID, TEST_HOUSE_ID);

    // then
    assertFalse(houseDeleted);
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityHouseRepository, never()).findByHouseId(TEST_HOUSE_ID);
    verify(communityRepository, never()).save(testCommunity);
  }

  @Test
  void removeHouseFromCommunityByHouseIdHouseNotExists() {
    // given
    Community testCommunity = getTestCommunity();

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityHouseRepository.findByHouseIdWithHouseMembers(TEST_HOUSE_ID))
        .willReturn(Optional.empty());

    // when
    boolean houseDeleted = communitySDJpaService.removeHouseFromCommunityByHouseId(TEST_COMMUNITY_ID, TEST_HOUSE_ID);

    // then
    assertFalse(houseDeleted);
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityHouseRepository).findByHouseIdWithHouseMembers(TEST_HOUSE_ID);
    verify(communityRepository, never()).save(testCommunity);
  }

  @Test
  void removeHouseFromCommunityByHouseIdHouseNotInCommunity() {
    // given
    Community testCommunity = getTestCommunity();

    given(communityRepository.findByCommunityIdWithHouses(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(testCommunity));
    given(communityHouseRepository.findByHouseIdWithHouseMembers(TEST_HOUSE_ID))
        .willReturn(Optional.empty());

    // when
    boolean houseDeleted = communitySDJpaService.removeHouseFromCommunityByHouseId(TEST_COMMUNITY_ID, TEST_HOUSE_ID);

    // then
    assertFalse(houseDeleted);
    verify(communityRepository).findByCommunityIdWithHouses(TEST_COMMUNITY_ID);
    verify(communityHouseRepository).findByHouseIdWithHouseMembers(TEST_HOUSE_ID);
    verify(communityRepository, never()).save(testCommunity);
  }

  private CommunityHouse getTestCommunityHouse() {
    return new CommunityHouse().withHouseId(TEST_HOUSE_ID);
  }

  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  private Set<CommunityHouse> getTestHouses(int count) {
    return Stream.iterate(0, n -> n + 1)
        .map(index -> new CommunityHouse(
            null,
            String.format("test-community-house-%s", index),
            generateUniqueId(),
            new HashSet<>()))
        .limit(count)
        .collect(Collectors.toSet());
  }

  private Set<User> getTestCommunityAdmins(int count) {
    return Stream.iterate(0, n -> n + 1)
        .map(index -> new User(
            TEST_ADMIN_NAME,
            generateUniqueId(),
            String.format("test-user-email-%s", index),
            String.format("test-user-password-%s", index),
            new HashSet<>())
        )
        .limit(count)
        .collect(Collectors.toSet());
  }

  private CommunityDto getTestCommunityDto() {
    CommunityDto testCommunityDto = new CommunityDto();
    testCommunityDto.setCommunityId(TEST_COMMUNITY_ID);
    testCommunityDto.setDistrict(TEST_COMMUNITY_DISTRICT);
    testCommunityDto.setName(TEST_COMMUNITY_NAME);
    return testCommunityDto;
  }

  private Community getTestCommunity() {
    Community testCommunity = new Community(
        getTestCommunityAdmins(TEST_ADMINS_COUNT),
        getTestHouses(TEST_HOUSES_COUNT),
        TEST_COMMUNITY_NAME,
        TEST_COMMUNITY_ID,
        TEST_COMMUNITY_DISTRICT,
        new HashSet<>()
    );
    return testCommunity;
  }

  private User getTestAdmin() {
    return new User(
        TEST_ADMIN_NAME,
        TEST_ADMIN_ID,
        TEST_ADMIN_EMAIL,
        TEST_ADMIN_PASSWORD,
        new HashSet<>());
  }

}
