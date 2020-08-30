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
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import java.util.Set;

import com.myhome.domain.User;
import org.aspectj.lang.annotation.After;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper
public interface SchedulePaymentApiMapper {

  @Mappings({
    @Mapping(source = "adminId", target = "admin", qualifiedByName = "adminIdToAdmin"),
    @Mapping(source = "memberId", target = "member", qualifiedByName = "memberIdToMember")
  })
  PaymentDto schedulePaymentRequestToPaymentDto(SchedulePaymentRequest schedulePaymentRequest);

  @Mappings({
    @Mapping(target = "admin", ignore = true),
    @Mapping(target = "member", ignore = true)
  })
  PaymentDto enrichedSchedulePaymentRequestToPaymentDto(EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest);

  @AfterMapping
  static void setUpUserAndAdmin(@MappingTarget PaymentDto paymentDto, EnrichedSchedulePaymentRequest enrichedSchedulePaymentRequest) {
    UserDto userDto = new UserDto();
    userDto.setId(enrichedSchedulePaymentRequest.getAdminEntityId());
    userDto.setUserId(enrichedSchedulePaymentRequest.getAdminId());
    userDto.setEncryptedPassword(enrichedSchedulePaymentRequest.getAdminEncryptedPassword());
    userDto.setName(enrichedSchedulePaymentRequest.getAdminName());
    userDto.setEmail(enrichedSchedulePaymentRequest.getAdminEmail());

    HouseMemberDto houseMemberDto = new HouseMemberDto();
    houseMemberDto.setId(enrichedSchedulePaymentRequest.getMemberEntityId());
    houseMemberDto.setMemberId(enrichedSchedulePaymentRequest.getMemberId());
    houseMemberDto.setName(enrichedSchedulePaymentRequest.getHouseMemberName());

    paymentDto.setAdmin(userDto);
    paymentDto.setMember(houseMemberDto);
  }

  @Named("adminIdToAdmin")
  static UserDto adminIdToAdminDto(String adminId) {
    UserDto userDto = new UserDto();
    userDto.setUserId(adminId);
    return userDto;
  }

  @Named("memberIdToMember")
  static HouseMemberDto memberIdToMemberDto(String memberId) {
    HouseMemberDto houseMemberDto = new HouseMemberDto();
    houseMemberDto.setMemberId(memberId);
    return houseMemberDto;
  }

  Set<PaymentDto> paymentSetToRestApiResponsePaymentSet(Set<Payment> payments);

  Set<ListMemberPaymentsResponse.MemberPayment> memberPaymentSetToRestApiResponseMemberPaymentSet(
      Set<Payment> memberPaymentSet);

  Set<ListAdminPaymentsResponse.AdminPayment> adminPaymentSetToRestApiResponseAdminPaymentSet(
      Set<Payment> memberPaymentSet);

  @Mappings({
    @Mapping(source = "admin", target = "adminId", qualifiedByName = "adminToAdminId"),
    @Mapping(source = "member", target = "memberId", qualifiedByName = "memberToMemberId")
  })
  SchedulePaymentResponse paymentToSchedulePaymentResponse(PaymentDto payment);

  @Named("adminToAdminId")
  static String adminToAdminId(UserDto userDto) {
    return userDto.getUserId();
  }

  @Named("memberToMemberId")
  static String memberToMemberId(HouseMemberDto houseMemberDto) {
    return houseMemberDto.getMemberId();
  }
}
