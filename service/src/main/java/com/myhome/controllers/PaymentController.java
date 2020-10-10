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

import com.myhome.controllers.mapper.SchedulePaymentApiMapper;
import com.myhome.controllers.request.SchedulePaymentRequest;
import com.myhome.controllers.response.ListAdminPaymentsResponse;
import com.myhome.controllers.response.ListMemberPaymentsResponse;
import com.myhome.controllers.response.SchedulePaymentResponse;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.services.CommunityService;
import com.myhome.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller which provides endpoints for managing payments
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
  private final PaymentService paymentService;
  private final CommunityService communityService;
  private final SchedulePaymentApiMapper schedulePaymentApiMapper;

  @Operation(description = "Schedule a new payment")
  @PostMapping(
      path = "/payments",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<SchedulePaymentResponse> schedulePayment(@Valid @RequestBody
      SchedulePaymentRequest request) {
    log.trace("Received schedule payment request");

    Optional<HouseMember> houseMember = paymentService.getHouseMember(request.getMemberId());
    Optional<User> adminOptional = communityService.findCommunityAdminById(request.getAdminId());

    return houseMember.flatMap(member -> adminOptional.filter(
        admin -> isUserAdminOfCommunityHouse(member.getCommunityHouse(), admin)))
        .map(admin -> schedulePaymentApiMapper.enrichedSchedulePaymentRequestToPaymentDto(
            schedulePaymentApiMapper.enrichSchedulePaymentRequest(request, admin,
                houseMember.get())))
        .map(paymentService::schedulePayment)
        .map(schedulePaymentApiMapper::paymentToSchedulePaymentResponse)
        .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private Boolean isUserAdminOfCommunityHouse(CommunityHouse communityHouse, User admin) {
    return communityHouse.getCommunity()
        .getAdmins()
        .contains(admin);
  }

  @Operation(description = "Get details about a payment with the given payment id")
  @GetMapping(
      path = "/payments/{paymentId}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<SchedulePaymentResponse> listPaymentDetails(
      @PathVariable String paymentId) {
    log.trace("Received request to get details about a payment with id[{}]", paymentId);

    return paymentService.getPaymentDetails(paymentId)
        .map(schedulePaymentApiMapper::paymentToSchedulePaymentResponse)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(description = "Get all payments for the specified member")
  @GetMapping(
      path = "/members/{memberId}/payments",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<ListMemberPaymentsResponse> listAllMemberPayments(
      @PathVariable String memberId) {
    log.trace("Received request to list all the payments for the house member with id[{}]",
        memberId);

    return paymentService.getHouseMember(memberId)
        .map(payments -> paymentService.getPaymentsByMember(memberId))
        .map(schedulePaymentApiMapper::memberPaymentSetToRestApiResponseMemberPaymentSet)
        .map(ListMemberPaymentsResponse::new)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(description = "Get all payments scheduled by the specified admin")
  @GetMapping(
      path = "/communities/{communityId}/admins/{adminId}/payments",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<ListAdminPaymentsResponse> listAllAdminScheduledPayments(
      @PathVariable String communityId, @PathVariable String adminId) {
    log.trace("Received request to list all the payments scheduled by the admin with id[{}]",
        adminId);

    Set<Payment> payments = paymentService.getPaymentsByAdmin(adminId);

    return communityService.getCommunityDetailsByIdWithAdmins(communityId)
        .map(community -> isAdminMatchingId(community.getAdmins(), adminId))
        .map(paymentsMatch -> isAdminMatchingPayment(payments, adminId))
        .map(matched -> schedulePaymentApiMapper.adminPaymentSetToRestApiResponseAdminPaymentSet(
            payments))
        .map(ListAdminPaymentsResponse::new)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private Boolean isAdminMatchingId(Set<User> list, String adminId) {
    if (list.stream()
        .anyMatch(communityAdmin -> communityAdmin.getUserId().equals(adminId))) {
      return true;
    }

    return null;
  }

  private Boolean isAdminMatchingPayment(Set<Payment> payments, String adminId) {
    if (payments.stream()
        .anyMatch(payment -> payment.getAdmin().getUserId().equals(adminId))) {
      return true;
    }

    return null;
  }
}
