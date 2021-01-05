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

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.mapper.SchedulePaymentApiMapper;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.model.HouseMemberDto;
import com.myhome.model.ListMemberPaymentsResponse;
import com.myhome.model.MemberPayment;
import com.myhome.services.HouseMemberDocumentService;
import com.myhome.services.PaymentService;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class HouseMemberDocumentTest {

  private static final String TEST_MEMBER_ID = "2";
  private static final String MEMBER_ID = "test-member-id";
  private static final String TEST_TYPE = "WATER BILL";
  private static final String TEST_DESCRIPTION = "This is your excess water bill";
  private static final boolean TEST_RECURRING = false;
  private static final BigDecimal TEST_CHARGE = BigDecimal.valueOf(50.00);
  private static final String TEST_DUE_DATE = "2020-08-15";
  private static final String TEST_ADMIN_ID = "1";
  private static final String TEST_COMMUNITY_ID = "4";
  private static final String TEST_ADMIN_PASSWORD = "password";
  private static final String TEST_ADMIN_NAME = "test_admin_name";
  private static final String TEST_ADMIN_EMAIL = "test_admin_email@myhome.com";
  private static final String TEST_MEMBER_NAME = "Test Name";
  private static final String TEST_COMMUNITY_NAME = "Test Community";
  private static final String TEST_COMMUNITY_DISTRICT = "Wonderland";
  private static final String COMMUNITY_ADMIN_NAME = "Test Name";
  private static final String COMMUNITY_ADMIN_EMAIL = "testadmin@myhome.com";
  private static final String COMMUNITY_ADMIN_PASSWORD = "testpassword@myhome.com";
  private static final String TEST_ID = "3";
  private static final String COMMUNITY_HOUSE_NAME = "Test House";
  private static final String COMMUNITY_HOUSE_ID = "5";

  private static final MockMultipartFile MULTIPART_FILE =
      new MockMultipartFile("memberDocument", new byte[0]);
  private static final HouseMemberDocument MEMBER_DOCUMENT =
      new HouseMemberDocument(MULTIPART_FILE.getName(), new byte[0]);

  @Mock
  private HouseMemberDocumentService houseMemberDocumentService;

  @Mock
  private SchedulePaymentApiMapper paymentApiMapper;

  @Mock
  private PaymentService paymentService;

  @InjectMocks
  private HouseMemberDocumentController houseMemberDocumentController;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void shouldGetDocumentSuccess() {
    // given
    given(houseMemberDocumentService.findHouseMemberDocument(MEMBER_ID))
        .willReturn(Optional.of(MEMBER_DOCUMENT));
    // when
    ResponseEntity<byte[]> responseEntity =
        houseMemberDocumentController.getHouseMemberDocument(MEMBER_ID);
    //then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(MEMBER_DOCUMENT.getDocumentContent(), responseEntity.getBody());
    assertEquals(MediaType.IMAGE_JPEG, responseEntity.getHeaders().getContentType());
    verify(houseMemberDocumentService).findHouseMemberDocument(MEMBER_ID);
  }

  @Test
  void shouldGetDocumentFailure() {
    // given
    given(houseMemberDocumentService.findHouseMemberDocument(MEMBER_ID))
        .willReturn(Optional.empty());
    // when
    ResponseEntity<byte[]> responseEntity =
        houseMemberDocumentController.getHouseMemberDocument(MEMBER_ID);
    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(houseMemberDocumentService).findHouseMemberDocument(MEMBER_ID);
  }

  @Test
  void shouldPostDocumentSuccess() throws IOException {
    // given
    given(houseMemberDocumentService.createHouseMemberDocument(MULTIPART_FILE, MEMBER_ID))
        .willReturn(Optional.of(MEMBER_DOCUMENT));
    // when
    ResponseEntity<byte[]> responseEntity =
        houseMemberDocumentController.uploadHouseMemberDocument(MEMBER_ID, MULTIPART_FILE);
    //then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(houseMemberDocumentService).createHouseMemberDocument(MULTIPART_FILE, MEMBER_ID);
  }

  @Test
  void shouldPostDocumentFailureNotFound() throws IOException {
    // given
    given(houseMemberDocumentService.createHouseMemberDocument(MULTIPART_FILE, MEMBER_ID))
        .willReturn(Optional.empty());
    // when
    ResponseEntity<byte[]> responseEntity =
        houseMemberDocumentController.uploadHouseMemberDocument(MEMBER_ID, MULTIPART_FILE);
    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(houseMemberDocumentService).createHouseMemberDocument(MULTIPART_FILE, MEMBER_ID);
  }

  @Test
  void shouldPutDocumentSuccess() throws IOException {
    // given
    given(houseMemberDocumentService.updateHouseMemberDocument(MULTIPART_FILE, MEMBER_ID))
        .willReturn(Optional.of(MEMBER_DOCUMENT));
    // when
    ResponseEntity<byte[]> responseEntity =
        houseMemberDocumentController.updateHouseMemberDocument(MEMBER_ID, MULTIPART_FILE);
    //then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(houseMemberDocumentService).updateHouseMemberDocument(MULTIPART_FILE, MEMBER_ID);
  }

  @Test
  void shouldPutDocumentFailureNotFound() throws IOException {
    // given
    given(houseMemberDocumentService.updateHouseMemberDocument(MULTIPART_FILE, MEMBER_ID))
        .willReturn(Optional.empty());
    // when
    ResponseEntity<byte[]> responseEntity =
        houseMemberDocumentController.updateHouseMemberDocument(MEMBER_ID, MULTIPART_FILE);
    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(houseMemberDocumentService).updateHouseMemberDocument(MULTIPART_FILE, MEMBER_ID);
  }

  @Test
  void shouldDeleteDocumentSuccess() {
    // given
    given(houseMemberDocumentService.deleteHouseMemberDocument(MEMBER_ID))
        .willReturn(true);
    // when
    ResponseEntity responseEntity =
        houseMemberDocumentController.deleteHouseMemberDocument(MEMBER_ID);
    //then
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    verify(houseMemberDocumentService).deleteHouseMemberDocument(MEMBER_ID);
  }

  @Test
  void shouldDeleteDocumentFailureNotFound() {
    // given
    given(houseMemberDocumentService.deleteHouseMemberDocument(MEMBER_ID))
        .willReturn(false);
    // when
    ResponseEntity responseEntity =
        houseMemberDocumentController.deleteHouseMemberDocument(MEMBER_ID);
    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(houseMemberDocumentService).deleteHouseMemberDocument(MEMBER_ID);
  }

  @Test
  void shouldGetNoMemberPaymentsSuccess() {
    //given
    given(paymentService.getHouseMember(TEST_MEMBER_ID))
        .willReturn(Optional.empty());

    //when
    ResponseEntity<ListMemberPaymentsResponse> responseEntity = houseMemberDocumentController.listAllMemberPayments(TEST_MEMBER_ID);

    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verifyNoInteractions(paymentApiMapper);
  }

  private PaymentDto createTestPaymentDto() {
    UserDto userDto = UserDto.builder()
        .userId(TEST_ADMIN_ID)
        .communityIds(new HashSet<>(Arrays.asList(TEST_COMMUNITY_ID)))
        .id(Long.valueOf(TEST_ADMIN_ID))
        .encryptedPassword(TEST_ADMIN_PASSWORD)
        .name(TEST_ADMIN_NAME)
        .email(TEST_ADMIN_EMAIL)
        .build();
    HouseMemberDto houseMemberDto = new HouseMemberDto()
        .memberId(TEST_MEMBER_ID)
        .name(TEST_MEMBER_NAME)
        .id(Long.valueOf(TEST_MEMBER_ID));

    return PaymentDto.builder()
        .paymentId(TEST_ID)
        .type(TEST_TYPE)
        .description(TEST_DESCRIPTION)
        .charge(TEST_CHARGE)
        .dueDate(TEST_DUE_DATE)
        .recurring(TEST_RECURRING)
        .admin(userDto)
        .member(houseMemberDto)
        .build();
  }

  private Community getMockCommunity(Set<User> admins) {
    Community community =
        new Community(admins, new HashSet<>(), TEST_COMMUNITY_NAME, TEST_COMMUNITY_ID,
            TEST_COMMUNITY_DISTRICT, new HashSet<>());
    User admin = new User(COMMUNITY_ADMIN_NAME, TEST_ADMIN_ID, COMMUNITY_ADMIN_EMAIL, false,
        COMMUNITY_ADMIN_PASSWORD, new HashSet<>(), new HashSet<>());
    community.getAdmins().add(admin);
    admin.getCommunities().add(community);

    CommunityHouse communityHouse = getMockCommunityHouse();
    communityHouse.setCommunity(community);
    community.getHouses().add(communityHouse);

    return community;
  }

  private CommunityHouse getMockCommunityHouse() {
    CommunityHouse communityHouse = new CommunityHouse();
    communityHouse.setName(COMMUNITY_HOUSE_NAME);
    communityHouse.setHouseId(COMMUNITY_HOUSE_ID);
    communityHouse.setHouseMembers(new HashSet<>());

    return communityHouse;
  }

  private Payment getMockPayment() {
    User admin = new User(TEST_ADMIN_NAME, TEST_ADMIN_ID, TEST_ADMIN_EMAIL, false, TEST_ADMIN_PASSWORD,
        new HashSet<>(), new HashSet<>());
    Community community = getMockCommunity(new HashSet<>());
    community.getAdmins().add(admin);
    admin.getCommunities().add(community);
    return new Payment(TEST_ID, TEST_CHARGE, TEST_TYPE, TEST_DESCRIPTION, TEST_RECURRING,
        LocalDate.parse(TEST_DUE_DATE, DateTimeFormatter.ofPattern("yyyy-MM-dd")), admin,
        new HouseMember(TEST_MEMBER_ID, new HouseMemberDocument(), TEST_MEMBER_NAME,
            new CommunityHouse()));
  }

  @Test
  void shouldGetMemberPaymentsSuccess() {
    // given
    PaymentDto paymentDto = createTestPaymentDto();

    given(paymentService.schedulePayment(paymentDto))
        .willReturn(paymentDto);

    HouseMember member = new HouseMember(TEST_MEMBER_ID, null, TEST_MEMBER_NAME, null);
    given(paymentService.getHouseMember(TEST_MEMBER_ID))
        .willReturn(Optional.of(member));

    Set<Payment> payments = new HashSet<>();
    Payment mockPayment = getMockPayment();
    payments.add(mockPayment);

    given(paymentService.getPaymentsByMember(TEST_MEMBER_ID))
        .willReturn(payments);

    Set<MemberPayment> paymentResponses = new HashSet<>();
    paymentResponses.add(
        new MemberPayment()
            .memberId(TEST_MEMBER_ID)
            .paymentId(TEST_ID)
            .charge(TEST_CHARGE)
            .dueDate(TEST_DUE_DATE));


    ListMemberPaymentsResponse expectedResponse = new ListMemberPaymentsResponse().payments(paymentResponses);

    given(paymentApiMapper.memberPaymentSetToRestApiResponseMemberPaymentSet(payments))
        .willReturn(paymentResponses);

    // when
    ResponseEntity<ListMemberPaymentsResponse> responseEntity = houseMemberDocumentController.listAllMemberPayments(TEST_MEMBER_ID);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(responseEntity.getBody(), expectedResponse);
    verify(paymentService).getPaymentsByMember(TEST_MEMBER_ID);
    verify(paymentApiMapper).memberPaymentSetToRestApiResponseMemberPaymentSet(payments);
  }
}
