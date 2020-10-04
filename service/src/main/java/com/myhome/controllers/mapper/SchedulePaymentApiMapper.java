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

package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.HouseMemberDto;
import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.request.EnrichedSchedulePaymentRequest;
import com.myhome.controllers.request.SchedulePaymentRequest;
import com.myhome.controllers.response.ListAdminPaymentsResponse;
import com.myhome.controllers.response.ListMemberPaymentsResponse;
import com.myhome.controllers.response.SchedulePaymentResponse;
import com.myhome.domain.Community;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper
public interface SchedulePaymentApiMapper {

  @AfterMapping
  static void setUpUserAndAdmin(@MappingTarget PaymentDto paymentDto,
      EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest) {
    UserDto userDto = UserDto.builder()
        .id(enrichedSchedulePaymentRequest.getAdminEntityId())
        .userId(enrichedSchedulePaymentRequest.getAdminId())
        .encryptedPassword(enrichedSchedulePaymentRequest.getAdminEncryptedPassword())
        .name(enrichedSchedulePaymentRequest.getAdminName())
        .email(enrichedSchedulePaymentRequest.getAdminEmail())
        .communityIds(enrichedSchedulePaymentRequest.getAdminCommunityIds())
        .build();

    HouseMemberDto houseMemberDto = HouseMemberDto.builder()
        .id(enrichedSchedulePaymentRequest.getMemberEntityId())
        .memberId(enrichedSchedulePaymentRequest.getMemberId())
        .name(enrichedSchedulePaymentRequest.getHouseMemberName())
        .build();

    paymentDto.setAdmin(userDto);
    paymentDto.setMember(houseMemberDto);
  }

  @Named("adminIdToAdmin")
  static UserDto adminIdToAdminDto(String adminId) {
    return UserDto.builder()
        .userId(adminId)
        .build();
  }

  @Named("memberIdToMember")
  static HouseMemberDto memberIdToMemberDto(String memberId) {
    return HouseMemberDto.builder()
        .memberId(memberId)
        .build();
  }

  @Named("adminToAdminId")
  static String adminToAdminId(UserDto userDto) {
    return userDto.getUserId();
  }

  @Named("memberToMemberId")
  static String memberToMemberId(HouseMemberDto houseMemberDto) {
    return houseMemberDto.getMemberId();
  }

  @Mappings({
      @Mapping(source = "adminId", target = "admin", qualifiedByName = "adminIdToAdmin"),
      @Mapping(source = "memberId", target = "member", qualifiedByName = "memberIdToMember")
  })
  PaymentDto schedulePaymentRequestToPaymentDto(SchedulePaymentRequest schedulePaymentRequest);

  @Mappings({
      @Mapping(target = "admin", ignore = true),
      @Mapping(target = "member", ignore = true)
  })
  PaymentDto enrichedSchedulePaymentRequestToPaymentDto(
      EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest);

  Set<ListMemberPaymentsResponse.MemberPayment> memberPaymentSetToRestApiResponseMemberPaymentSet(
      Set<Payment> memberPaymentSet);

  @Mapping(target = "memberId", expression = "java(payment.getMember().getMemberId())")
  ListMemberPaymentsResponse.MemberPayment paymentToMemberPayment(Payment payment);

  Set<ListAdminPaymentsResponse.AdminPayment> adminPaymentSetToRestApiResponseAdminPaymentSet(
      Set<Payment> memberPaymentSet);

  @Mapping(target = "adminId", expression = "java(payment.getAdmin().getUserId())")
  ListAdminPaymentsResponse.AdminPayment paymentToAdminPayment(Payment payment);

  @Mappings({
      @Mapping(source = "admin", target = "adminId", qualifiedByName = "adminToAdminId"),
      @Mapping(source = "member", target = "memberId", qualifiedByName = "memberToMemberId")
  })
  SchedulePaymentResponse paymentToSchedulePaymentResponse(PaymentDto payment);

  default EnrichedSchedulePaymentRequest enrichSchedulePaymentRequest(
      SchedulePaymentRequest request, User admin, HouseMember member) {
    Set<String> communityIds = admin.getCommunities()
        .stream()
        .map(Community::getCommunityId)
        .collect(Collectors.toSet());
    return new EnrichedSchedulePaymentRequest(request.getType(),
        request.getDescription(),
        request.isRecurring(),
        request.getCharge(),
        request.getDueDate(),
        request.getAdminId(),
        admin.getId(),
        admin.getName(),
        admin.getEmail(),
        admin.getEncryptedPassword(),
        communityIds,
        member.getMemberId(),
        member.getId(),
        member.getHouseMemberDocument() != null ? member.getHouseMemberDocument()
            .getDocumentFilename() : "",
        member.getName(),
        member.getCommunityHouse() != null ? member.getCommunityHouse().getHouseId() : "");
  }
}
