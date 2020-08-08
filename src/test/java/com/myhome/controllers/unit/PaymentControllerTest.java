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

import com.myhome.controllers.PaymentController;
import com.myhome.controllers.dto.CommunityDto;
import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.mapper.SchedulePaymentApiMapper;
import com.myhome.controllers.request.SchedulePaymentRequest;
import com.myhome.controllers.response.ListAdminPaymentsResponse;
import com.myhome.controllers.response.ListMemberPaymentsResponse;
import com.myhome.controllers.response.SchedulePaymentResponse;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityAdmin;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.services.CommunityService;
import com.myhome.services.PaymentService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class PaymentControllerTest {

  private static final String TEST_TYPE = "WATER BILL";
  private static final String TEST_DESCRIPTION = "This is your excess water bill";
  private static final boolean TEST_RECURRING = false;
  private static final BigDecimal TEST_CHARGE = BigDecimal.valueOf(50.00);
  private static final String TEST_DUE_DATE = "2020-08-15";
  private static final String TEST_MEMBER_NAME = "Test Name";
  private static final String TEST_COMMUNITY_NAME = "Test Community";
  private static final String TEST_COMMUNITY_DISTRICT = "Wonderland";
  private static final String TEST_ADMIN_ID = "1";
  private static final String TEST_MEMBER_ID = "2";
  private static final String TEST_ID = "3";
  private static final String TEST_COMMUNITY_ID = "4";

  @Mock
  private PaymentService paymentService;

  @Mock
  private SchedulePaymentApiMapper paymentApiMapper;

  @InjectMocks
  private PaymentController paymentController;

  @Mock
  private CommunityService communityService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  private PaymentDto createTestPaymentDto() {
    PaymentDto paymentDto = new PaymentDto();
    paymentDto.setType(TEST_TYPE);
    paymentDto.setDescription(TEST_DESCRIPTION);
    paymentDto.setCharge(TEST_CHARGE);
    paymentDto.setDueDate(TEST_DUE_DATE);
    paymentDto.setRecurring(TEST_RECURRING);
    paymentDto.setAdminId(TEST_ADMIN_ID);
    paymentDto.setMemberId(TEST_MEMBER_ID);
    return paymentDto;
  }

  private CommunityDto createTestCommunityDto() {
    CommunityDto communityDto = new CommunityDto();
    communityDto.setName(TEST_COMMUNITY_NAME);
    communityDto.setDistrict(TEST_COMMUNITY_DISTRICT);
    communityDto.setCommunityId(TEST_COMMUNITY_ID);
    return communityDto;
  }

  private Community getMockCommunity(Set<CommunityAdmin> admins) {
    Community community = new Community(admins, null, TEST_COMMUNITY_NAME, TEST_COMMUNITY_ID,
        TEST_COMMUNITY_DISTRICT, new HashSet<>());
    CommunityAdmin admin = new CommunityAdmin(new HashSet<>(), TEST_ADMIN_ID);
    community.getAdmins().add(admin);
    admin.getCommunities().add(community);
    return community;
  }

  private Payment getMockPayment() {
    return new Payment(TEST_ID, TEST_CHARGE, TEST_TYPE, TEST_DESCRIPTION, TEST_RECURRING,
        LocalDate.parse(TEST_DUE_DATE, DateTimeFormatter.ofPattern("yyyy-MM-dd")), TEST_ADMIN_ID,
        TEST_MEMBER_ID);
  }

  @Test
  void shouldSchedulePaymentSuccessful() {
    // given
    SchedulePaymentRequest request =
        new SchedulePaymentRequest(TEST_TYPE, TEST_DESCRIPTION, TEST_RECURRING, TEST_CHARGE,
            TEST_DUE_DATE, TEST_ADMIN_ID, TEST_MEMBER_ID);
    PaymentDto paymentDto = createTestPaymentDto();
    SchedulePaymentResponse response =
        new SchedulePaymentResponse(TEST_ID, TEST_CHARGE, TEST_TYPE, TEST_DESCRIPTION,
            TEST_RECURRING, TEST_DUE_DATE, TEST_ADMIN_ID, TEST_MEMBER_ID);

    HouseMember member = new HouseMember(TEST_MEMBER_ID, null, TEST_MEMBER_NAME, null);

    given(paymentApiMapper.schedulePaymentRequestToPaymentDto(request))
        .willReturn(paymentDto);
    given(paymentService.schedulePayment(paymentDto))
        .willReturn(paymentDto);
    given(paymentApiMapper.paymentToSchedulePaymentResponse(paymentDto))
        .willReturn(response);
    given(paymentService.getHouseMember(TEST_MEMBER_ID))
        .willReturn(Optional.of(member));

    //when
    ResponseEntity<SchedulePaymentResponse> responseEntity =
        paymentController.schedulePayment(request);
    //then
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertEquals(response, responseEntity.getBody());
    verify(paymentApiMapper).schedulePaymentRequestToPaymentDto(request);
    verify(paymentService).schedulePayment(paymentDto);
    verify(paymentApiMapper).paymentToSchedulePaymentResponse(paymentDto);
  }

  @Test
  void shouldNotScheduleIfMemberDoesNotExist() {
    // given
    SchedulePaymentRequest request =
        new SchedulePaymentRequest(TEST_TYPE, TEST_DESCRIPTION, TEST_RECURRING, TEST_CHARGE,
            TEST_DUE_DATE, TEST_ADMIN_ID, TEST_MEMBER_ID);
    PaymentDto paymentDto = createTestPaymentDto();

    given(paymentApiMapper.schedulePaymentRequestToPaymentDto(request))
        .willReturn(paymentDto);
    given(paymentService.schedulePayment(paymentDto))
        .willReturn(paymentDto);
    given(paymentService.getHouseMember(TEST_MEMBER_ID))
        .willReturn(Optional.empty());

    //when
    ResponseEntity<SchedulePaymentResponse> responseEntity =
        paymentController.schedulePayment(request);

    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verifyNoInteractions(paymentApiMapper);
  }

  @Test
  void shouldGetPaymentDetailsSuccess() {
    // given
    PaymentDto paymentDto = createTestPaymentDto();

    SchedulePaymentResponse expectedResponse = new SchedulePaymentResponse(
        TEST_ID,
        TEST_CHARGE,
        TEST_TYPE,
        TEST_DESCRIPTION,
        TEST_RECURRING,
        TEST_DUE_DATE,
        TEST_ADMIN_ID,
        TEST_MEMBER_ID
    );
    given(paymentService.getPaymentDetails(TEST_ID))
        .willReturn(Optional.of(paymentDto));
    given(paymentApiMapper.paymentToSchedulePaymentResponse(paymentDto))
        .willReturn(expectedResponse);

    // when
    ResponseEntity<SchedulePaymentResponse> responseEntity =
        paymentController.listPaymentDetails(TEST_ID);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedResponse, responseEntity.getBody());
    verify(paymentService).getPaymentDetails(TEST_ID);
    verify(paymentApiMapper).paymentToSchedulePaymentResponse(paymentDto);
  }

  @Test
  void shouldListNoPaymentDetailsSuccess() {
    //given
    given(paymentService.getPaymentDetails(TEST_ID))
        .willReturn(Optional.empty());

    //when
    ResponseEntity<SchedulePaymentResponse> responseEntity =
        paymentController.listPaymentDetails(TEST_ID);

    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verify(paymentService).getPaymentDetails(TEST_ID);
    verifyNoInteractions(paymentApiMapper);
  }

  @Test
  void shouldGetMemberPaymentsSuccess() {
    // given
    SchedulePaymentRequest request =
        new SchedulePaymentRequest(TEST_TYPE, TEST_DESCRIPTION, TEST_RECURRING, TEST_CHARGE,
            TEST_DUE_DATE, TEST_ADMIN_ID, TEST_MEMBER_ID);
    PaymentDto paymentDto = createTestPaymentDto();

    given(paymentService.schedulePayment(paymentDto))
        .willReturn(paymentDto);

    HouseMember member = new HouseMember(TEST_MEMBER_ID, null, TEST_MEMBER_NAME, null);
    given(paymentService.getHouseMember(TEST_MEMBER_ID))
        .willReturn(Optional.of(member));

    Set<Payment> payments = new HashSet<>();
    Payment mockPayment = getMockPayment();
    payments.add(mockPayment);

    paymentController.schedulePayment(request);

    given(paymentService.getPaymentsByMember(TEST_MEMBER_ID))
        .willReturn(payments);

    Set<ListMemberPaymentsResponse.MemberPayment> paymentResponses = new HashSet<>();
    paymentResponses.add(
        new ListMemberPaymentsResponse.MemberPayment(
            TEST_MEMBER_ID,
            TEST_ID,
            TEST_CHARGE,
            TEST_DUE_DATE
        )
    );

    ListMemberPaymentsResponse expectedResponse =
        new ListMemberPaymentsResponse(paymentResponses);

    given(paymentApiMapper.memberPaymentSetToRestApiResponseMemberPaymentSet(payments))
        .willReturn(paymentResponses);

    // when
    ResponseEntity<ListMemberPaymentsResponse> responseEntity =
        paymentController.listAllMemberPayments(TEST_MEMBER_ID);

    // then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(responseEntity.getBody(), expectedResponse);
    verify(paymentService).getPaymentsByMember(TEST_MEMBER_ID);
    verify(paymentApiMapper).memberPaymentSetToRestApiResponseMemberPaymentSet(payments);
  }

  @Test
  void shouldGetNoMemberPaymentsSuccess() {
    //given
    given(paymentService.getHouseMember(TEST_MEMBER_ID))
        .willReturn(Optional.empty());

    //when
    ResponseEntity<ListMemberPaymentsResponse> responseEntity =
        paymentController.listAllMemberPayments(TEST_MEMBER_ID);

    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verifyNoInteractions(paymentApiMapper);
  }

  @Test
  void shouldGetAdminPaymentsSuccess() {
    // given
    SchedulePaymentRequest request =
        new SchedulePaymentRequest(TEST_TYPE, TEST_DESCRIPTION, TEST_RECURRING, TEST_CHARGE,
            TEST_DUE_DATE, TEST_ADMIN_ID, TEST_MEMBER_ID);
    PaymentDto paymentDto = createTestPaymentDto();

    given(paymentService.schedulePayment(paymentDto))
        .willReturn(paymentDto);

    Set<Payment> payments = new HashSet<>();
    Payment mockPayment = getMockPayment();
    payments.add(mockPayment);

    paymentController.schedulePayment(request);

    Set<String> adminIds = new HashSet<>();
    adminIds.add(TEST_ADMIN_ID);

    Set<CommunityAdmin> admins = new HashSet<>();

    Community community = getMockCommunity(admins);

    CommunityDto communityDto = createTestCommunityDto();

    given(communityService.createCommunity(communityDto))
        .willReturn(community);
    given(communityService.getCommunityDetailsById(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(community));
    given(paymentService.getPaymentsByAdmin(TEST_ADMIN_ID))
        .willReturn(payments);
    given(communityService.addAdminsToCommunity(TEST_COMMUNITY_ID, adminIds))
        .willReturn(Optional.of(community));

    Set<ListAdminPaymentsResponse.AdminPayment> responsePayments = new HashSet<>();
    responsePayments.add(
        new ListAdminPaymentsResponse.AdminPayment(
            TEST_ADMIN_ID,
            TEST_ID,
            TEST_CHARGE,
            TEST_DUE_DATE
        )
    );

    ListAdminPaymentsResponse expectedResponse = new ListAdminPaymentsResponse(responsePayments);

    given(paymentApiMapper.adminPaymentSetToRestApiResponseAdminPaymentSet(payments))
        .willReturn(responsePayments);

    //when
    ResponseEntity<ListAdminPaymentsResponse> responseEntity =
        paymentController.listAllAdminScheduledPayments(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    //then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(expectedResponse, responseEntity.getBody());
    verify(paymentService).getPaymentsByAdmin(TEST_ADMIN_ID);
    verify(paymentApiMapper).adminPaymentSetToRestApiResponseAdminPaymentSet(payments);
  }

  @Test
  void shouldGetNoAdminPaymentDetailsCommunityNotFoundSuccess() {
    //given
    given(communityService.getCommunityDetailsById(TEST_COMMUNITY_ID))
        .willReturn(Optional.empty());

    //when
    ResponseEntity<ListAdminPaymentsResponse> responseEntity =
        paymentController.listAllAdminScheduledPayments(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertNull(responseEntity.getBody());
    verify(communityService).getCommunityDetailsById(TEST_COMMUNITY_ID);
    verifyNoInteractions(paymentApiMapper);
  }

  @Test
  void shouldGetNoAdminPaymentDetailsAdminNotFoundSuccess() {
    //given
    Community community =
        new Community(new HashSet<>(), new HashSet<>(), TEST_COMMUNITY_NAME, TEST_COMMUNITY_ID,
            TEST_COMMUNITY_DISTRICT, new HashSet<>());

    given(communityService.getCommunityDetailsById(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(community));
    given(paymentService.getPaymentsByAdmin(TEST_ADMIN_ID))
        .willReturn(new HashSet<>());

    //when
    ResponseEntity<ListAdminPaymentsResponse> response =
        paymentController.listAllAdminScheduledPayments(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    //then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(paymentService).getPaymentsByAdmin(TEST_ADMIN_ID);
    verifyNoInteractions(paymentApiMapper);
  }

  @Test
  void shouldGetNoPaymentsForFoundAdminSuccess() {
    //given
    Community community =
        new Community(new HashSet<>(), new HashSet<>(), TEST_COMMUNITY_NAME, TEST_COMMUNITY_ID,
            TEST_COMMUNITY_DISTRICT, new HashSet<>());
    CommunityAdmin admin = new CommunityAdmin(new HashSet<>(), TEST_ADMIN_ID);
    community.getAdmins().add(admin);
    admin.getCommunities().add(community);

    given(communityService.getCommunityDetailsById(TEST_COMMUNITY_ID))
        .willReturn(Optional.of(community));
    given(paymentService.getPaymentsByAdmin(TEST_ADMIN_ID))
        .willReturn(new HashSet<>());

    //when
    ResponseEntity<ListAdminPaymentsResponse> response =
        paymentController.listAllAdminScheduledPayments(TEST_COMMUNITY_ID, TEST_ADMIN_ID);

    //then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(paymentService).getPaymentsByAdmin(TEST_ADMIN_ID);
    verifyNoInteractions(paymentApiMapper);
  }
}
