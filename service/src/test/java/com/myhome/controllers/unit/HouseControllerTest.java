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

package com.myhome.controllers.unit;

import com.myhome.controllers.HouseController;
import com.myhome.controllers.dto.HouseMemberDto;
import com.myhome.controllers.dto.mapper.HouseMemberMapper;
import com.myhome.controllers.mapper.HouseApiMapper;
import com.myhome.controllers.request.AddHouseMemberRequest;
import com.myhome.controllers.response.AddHouseMemberResponse;
import com.myhome.controllers.response.GetHouseDetailsResponse;
import com.myhome.controllers.response.ListHouseMembersResponse;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.services.HouseService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class HouseControllerTest {

  private final String TEST_HOUSE_ID = "test-house-id";
  private final String TEST_MEMBER_ID = "test-member-id";

  private final int TEST_HOUSES_COUNT = 2;
  private final int TEST_HOUSE_MEMBERS_COUNT = 2;

  @Mock
  private HouseMemberMapper houseMemberMapper;
  @Mock
  private HouseService houseService;
  @Mock
  private HouseApiMapper houseApiMapper;

  @InjectMocks
  private HouseController houseController;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void listAllHouses() {
    // given
    Set<CommunityHouse> testHouses = getTestHouses(TEST_HOUSES_COUNT);
    Set<GetHouseDetailsResponse.CommunityHouse> testHousesResponse = testHouses.stream()
        .map(house -> new GetHouseDetailsResponse.CommunityHouse(house.getHouseId(),
            house.getName()))
        .collect(Collectors.toSet());
    GetHouseDetailsResponse expectedResponseBody = new GetHouseDetailsResponse();
    expectedResponseBody.setHouses(testHousesResponse);

    given(houseService.listAllHouses(any()))
        .willReturn(testHouses);
    given(houseApiMapper.communityHouseSetToRestApiResponseCommunityHouseSet(testHouses))
        .willReturn(testHousesResponse);

    // when
    ResponseEntity<GetHouseDetailsResponse> response = houseController.listAllHouses(null);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponseBody, response.getBody());
  }

  @Test
  void getHouseDetails() {
    // given
    CommunityHouse testCommunityHouse = getTestCommunityHouse();
    GetHouseDetailsResponse.CommunityHouse houseDetailsResponse =
        new GetHouseDetailsResponse.CommunityHouse(testCommunityHouse.getHouseId(),
            testCommunityHouse.getName());
    GetHouseDetailsResponse expectedResponseBody = new GetHouseDetailsResponse();
    expectedResponseBody.getHouses().add(houseDetailsResponse);

    given(houseService.getHouseDetailsById(TEST_HOUSE_ID))
        .willReturn(Optional.of(testCommunityHouse));
    given(houseApiMapper.communityHouseToRestApiResponseCommunityHouse(testCommunityHouse))
        .willReturn(houseDetailsResponse);

    // when
    ResponseEntity<GetHouseDetailsResponse> response =
        houseController.getHouseDetails(TEST_HOUSE_ID);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponseBody, response.getBody());
    verify(houseService).getHouseDetailsById(TEST_HOUSE_ID);
    verify(houseApiMapper).communityHouseToRestApiResponseCommunityHouse(testCommunityHouse);
  }

  @Test
  void getHouseDetailsNotExists() {
    // given
    CommunityHouse testCommunityHouse = getTestCommunityHouse();

    given(houseService.getHouseDetailsById(TEST_HOUSE_ID))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<GetHouseDetailsResponse> response =
        houseController.getHouseDetails(TEST_HOUSE_ID);

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(houseService).getHouseDetailsById(TEST_HOUSE_ID);
    verify(houseApiMapper, never()).communityHouseToRestApiResponseCommunityHouse(
        testCommunityHouse);
  }

  @Test
  void listAllMembersOfHouse() {
    // given
    List<HouseMember> testHouseMembers =
        new ArrayList<>(getTestHouseMembers(TEST_HOUSE_MEMBERS_COUNT));
    Set<ListHouseMembersResponse.HouseMember> testHouseMemberDetails = testHouseMembers.stream()
        .map(member -> new ListHouseMembersResponse.HouseMember(member.getMemberId(),
            member.getName()))
        .collect(Collectors.toSet());
    ListHouseMembersResponse expectedResponseBody =
        new ListHouseMembersResponse(testHouseMemberDetails);

    given(houseService.getHouseMembersById(TEST_HOUSE_ID, null))
        .willReturn(Optional.of(testHouseMembers));
    given(houseMemberMapper.houseMemberSetToRestApiResponseHouseMemberSet(
        new HashSet<>(testHouseMembers)))
        .willReturn(testHouseMemberDetails);

    // when
    ResponseEntity<ListHouseMembersResponse> response =
        houseController.listAllMembersOfHouse(TEST_HOUSE_ID, null);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResponseBody, response.getBody());
    verify(houseService).getHouseMembersById(TEST_HOUSE_ID, null);
    verify(houseMemberMapper).houseMemberSetToRestApiResponseHouseMemberSet(
        new HashSet<>(testHouseMembers));
  }

  @Test
  void listAllMembersOfHouseNotExists() {
    // given
    given(houseService.getHouseMembersById(TEST_HOUSE_ID, null))
        .willReturn(Optional.empty());

    // when
    ResponseEntity<ListHouseMembersResponse> response =
        houseController.listAllMembersOfHouse(TEST_HOUSE_ID, null);

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(houseService).getHouseMembersById(TEST_HOUSE_ID, null);
    verify(houseMemberMapper, never()).houseMemberSetToRestApiResponseHouseMemberSet(anySet());
  }

  @Test
  void addHouseMembers() {
    // given
    Set<HouseMember> testMembers = getTestHouseMembers(TEST_HOUSE_MEMBERS_COUNT);
    Set<HouseMemberDto> testMembersDto = testMembers.stream()
        .map(member -> HouseMemberDto.builder()
            .memberId(member.getMemberId())
            .name(member.getName())
            .build())
        .collect(Collectors.toSet());

    AddHouseMemberRequest request = new AddHouseMemberRequest(testMembersDto);

    Set<AddHouseMemberResponse.HouseMember> addedMembers = testMembers.stream()
        .map(member -> new AddHouseMemberResponse.HouseMember(member.getMemberId(),
            member.getName()))
        .collect(Collectors.toSet());

    AddHouseMemberResponse expectedResponseBody = new AddHouseMemberResponse();
    expectedResponseBody.setMembers(addedMembers);

    given(houseMemberMapper.houseMemberDtoSetToHouseMemberSet(testMembersDto))
        .willReturn(testMembers);
    given(houseService.addHouseMembers(TEST_HOUSE_ID, testMembers)).
        willReturn(testMembers);
    given(houseMemberMapper.houseMemberSetToRestApiResponseAddHouseMemberSet(testMembers))
        .willReturn(addedMembers);

    // when
    ResponseEntity<AddHouseMemberResponse> response =
        houseController.addHouseMembers(TEST_HOUSE_ID, request);

    // then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(expectedResponseBody, response.getBody());
    verify(houseMemberMapper).houseMemberDtoSetToHouseMemberSet(testMembersDto);
    verify(houseService).addHouseMembers(TEST_HOUSE_ID, testMembers);
    verify(houseMemberMapper).houseMemberSetToRestApiResponseAddHouseMemberSet(testMembers);
  }

  @Test
  void addHouseMembersNoMembersAdded() {
    // given
    Set<HouseMember> testMembers = getTestHouseMembers(TEST_HOUSE_MEMBERS_COUNT);
    Set<HouseMemberDto> testMembersDto = testMembers.stream()
        .map(member -> HouseMemberDto.builder()
            .memberId(member.getMemberId())
            .name(member.getName())
            .build())
        .collect(Collectors.toSet());

    AddHouseMemberRequest request = new AddHouseMemberRequest(testMembersDto);

    Set<AddHouseMemberResponse.HouseMember> addedMembers = testMembers.stream()
        .map(member -> new AddHouseMemberResponse.HouseMember(member.getMemberId(),
            member.getName()))
        .collect(Collectors.toSet());

    AddHouseMemberResponse expectedResponseBody = new AddHouseMemberResponse();
    expectedResponseBody.setMembers(addedMembers);

    given(houseMemberMapper.houseMemberDtoSetToHouseMemberSet(testMembersDto))
        .willReturn(testMembers);
    given(houseService.addHouseMembers(TEST_HOUSE_ID, testMembers)).
        willReturn(new HashSet<>());

    // when
    ResponseEntity<AddHouseMemberResponse> response =
        houseController.addHouseMembers(TEST_HOUSE_ID, request);

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(houseMemberMapper).houseMemberDtoSetToHouseMemberSet(testMembersDto);
    verify(houseService).addHouseMembers(TEST_HOUSE_ID, testMembers);
    verify(houseMemberMapper, never()).houseMemberSetToRestApiResponseAddHouseMemberSet(
        testMembers);
  }

  @Test
  void deleteHouseMemberSuccess() {
    // given
    given(houseService.deleteMemberFromHouse(TEST_HOUSE_ID, TEST_MEMBER_ID))
        .willReturn(true);
    // when
    ResponseEntity<Void> response =
        houseController.deleteHouseMember(TEST_HOUSE_ID, TEST_MEMBER_ID);

    // then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void deleteHouseMemberFailure() {
    // given
    given(houseService.deleteMemberFromHouse(TEST_HOUSE_ID, TEST_MEMBER_ID))
        .willReturn(false);

    // when
    ResponseEntity<Void> response =
        houseController.deleteHouseMember(TEST_HOUSE_ID, TEST_MEMBER_ID);

    // then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
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
            new HashSet<>(),
            new HashSet<>()))
        .limit(count)
        .collect(Collectors.toSet());
  }

  private Set<HouseMember> getTestHouseMembers(int count) {
    return Stream
        .generate(() -> new HouseMember()
            .withMemberId(generateUniqueId()))
        .limit(count)
        .collect(Collectors.toSet());
  }

  private CommunityHouse getTestCommunityHouse() {
    return new CommunityHouse().withHouseId(TEST_HOUSE_ID);
  }
}