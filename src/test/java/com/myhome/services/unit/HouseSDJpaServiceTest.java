package com.myhome.services.unit;

import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.HouseMemberDocumentRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.springdatajpa.HouseSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class HouseSDJpaServiceTest {

  private final int TEST_HOUSES_COUNT = 10;
  private final int TEST_HOUSE_MEMBERS_COUNT = 10;
  private final String HOUSE_ID = "test-house-id";
  private final String MEMBER_ID = "test-member-id";

  @Mock
  private HouseMemberRepository houseMemberRepository;
  @Mock
  private HouseMemberDocumentRepository houseMemberDocumentRepository;
  @Mock
  private CommunityHouseRepository communityHouseRepository;
  @InjectMocks
  private HouseSDJpaService houseSDJpaService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void listAllHousesDefault() {
    // given
    Set<CommunityHouse> housesInDatabase = getTestHouses(TEST_HOUSES_COUNT);
    given(communityHouseRepository.findAll())
        .willReturn(housesInDatabase);

    // when
    Set<CommunityHouse> resultHouses = houseSDJpaService.listAllHouses();

    // then
    assertEquals(housesInDatabase, resultHouses);
    verify(communityHouseRepository).findAll();
  }

  @Test
  void listAllHousesCustomPageable() {
    // given
    Set<CommunityHouse> housesInDatabase = getTestHouses(TEST_HOUSES_COUNT);
    Pageable pageRequest = PageRequest.of(0, TEST_HOUSES_COUNT);
    Page<CommunityHouse> housesPage = new PageImpl<>(
        new ArrayList<>(housesInDatabase),
        pageRequest,
        TEST_HOUSES_COUNT
    );
    given(communityHouseRepository.findAll(pageRequest))
        .willReturn(housesPage);

    // when
    Set<CommunityHouse> resultHouses = houseSDJpaService.listAllHouses(pageRequest);

    // then
    assertEquals(housesInDatabase, resultHouses);
    verify(communityHouseRepository).findAll(pageRequest);
  }

  @Test
  void addHouseMembers() {
    // given
    Set<HouseMember> membersToAdd = getTestHouseMembers(TEST_HOUSE_MEMBERS_COUNT);
    int membersToAddSize = membersToAdd.size();
    CommunityHouse communityHouse = getTestCommunityHouse();

    given(communityHouseRepository.findByHouseId(HOUSE_ID))
        .willReturn(communityHouse);
    given(houseMemberRepository.saveAll(membersToAdd))
        .willReturn(membersToAdd);

    // when
    Set<HouseMember> resultMembers = houseSDJpaService.addHouseMembers(HOUSE_ID, membersToAdd);

    // then
    assertEquals(membersToAddSize, resultMembers.size());
    assertEquals(membersToAddSize, communityHouse.getHouseMembers().size());
    verify(communityHouseRepository).save(communityHouse);
    verify(houseMemberRepository).saveAll(membersToAdd);
    verify(communityHouseRepository).findByHouseId(HOUSE_ID);
  }

  @Test
  void addHouseMembersHouseNotExists() {
    // given
    Set<HouseMember> membersToAdd = getTestHouseMembers(TEST_HOUSE_MEMBERS_COUNT);

    given(communityHouseRepository.findByHouseId(HOUSE_ID))
        .willReturn(null);

    // when
    Set<HouseMember> resultMembers = houseSDJpaService.addHouseMembers(HOUSE_ID, membersToAdd);

    // then
    assertTrue(resultMembers.isEmpty());
    verify(communityHouseRepository).findByHouseId(HOUSE_ID);
    verify(communityHouseRepository, never()).save(any());
    verifyNoInteractions(houseMemberRepository);
  }

  @Test
  void deleteMemberFromHouse() {
    // given
    Set<HouseMember> houseMembers = getTestHouseMembers(TEST_HOUSE_MEMBERS_COUNT);
    CommunityHouse communityHouse = getTestCommunityHouse();

    HouseMember memberToDelete = new HouseMember().withMemberId(MEMBER_ID);
    memberToDelete.setCommunityHouse(communityHouse);

    houseMembers.add(memberToDelete);
    communityHouse.setHouseMembers(houseMembers);

    given(communityHouseRepository.findByHouseId(HOUSE_ID))
        .willReturn(communityHouse);

    // when
    boolean isMemberDeleted = houseSDJpaService.deleteMemberFromHouse(HOUSE_ID, MEMBER_ID);

    // then
    assertTrue(isMemberDeleted);
    assertNull(memberToDelete.getCommunityHouse());
    assertFalse(communityHouse.getHouseMembers().contains(memberToDelete));
    verify(communityHouseRepository).findByHouseId(HOUSE_ID);
    verify(communityHouseRepository).save(communityHouse);
    verify(houseMemberRepository).save(memberToDelete);
  }

  @Test
  void deleteMemberFromHouseNotExists() {
    // given
    given(communityHouseRepository.findByHouseId(HOUSE_ID))
        .willReturn(null);

    // when
    boolean isMemberDeleted = houseSDJpaService.deleteMemberFromHouse(HOUSE_ID, MEMBER_ID);

    // then
    assertFalse(isMemberDeleted);
    verify(communityHouseRepository).findByHouseId(HOUSE_ID);
    verify(communityHouseRepository, never()).save(any());
    verifyNoInteractions(houseMemberRepository);
  }

  @Test
  void deleteMemberFromHouseMemberNotPresent() {
    // given
    Set<HouseMember> houseMembers = getTestHouseMembers(TEST_HOUSE_MEMBERS_COUNT);
    CommunityHouse communityHouse = getTestCommunityHouse();

    communityHouse.setHouseMembers(houseMembers);

    given(communityHouseRepository.findByHouseId(HOUSE_ID))
        .willReturn(communityHouse);

    // when
    boolean isMemberDeleted = houseSDJpaService.deleteMemberFromHouse(HOUSE_ID, MEMBER_ID);

    // then
    assertFalse(isMemberDeleted);
    verify(communityHouseRepository).findByHouseId(HOUSE_ID);
    verify(communityHouseRepository, never()).save(communityHouse);
    verifyNoInteractions(houseMemberRepository);
  }

  @Test
  void getHouseDetailsById() {
    // given

    // when

    // then
  }

  @Test
  void getHouseMembersById() {
    // given

    // when

    // then
  }

  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  private Set<CommunityHouse> getTestHouses(int count) {
    return Stream
        .generate(() -> new CommunityHouse().withHouseId(generateUniqueId()))
        .limit(count)
        .collect(Collectors.toSet());
  }

  private Set<HouseMember> getTestHouseMembers(int count) {
    return Stream
        .generate(() -> new HouseMember().withMemberId(generateUniqueId()))
        .limit(count)
        .collect(Collectors.toSet());
  }

  private CommunityHouse getTestCommunityHouse() {
    return new CommunityHouse();
  }
}