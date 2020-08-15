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

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.request.SchedulePaymentRequest;
import com.myhome.controllers.response.ListAdminPaymentsResponse;
import com.myhome.controllers.response.ListMemberPaymentsResponse;
import com.myhome.controllers.response.SchedulePaymentResponse;
import com.myhome.domain.Payment;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface SchedulePaymentApiMapper {

  PaymentDto schedulePaymentRequestToPaymentDto(SchedulePaymentRequest schedulePaymentRequest);

  Set<PaymentDto> paymentSetToRestApiResponsePaymentSet(Set<Payment> payments);

  Set<ListMemberPaymentsResponse.MemberPayment> memberPaymentSetToRestApiResponseMemberPaymentSet(
      Set<Payment> memberPaymentSet);

  Set<ListAdminPaymentsResponse.AdminPayment> adminPaymentSetToRestApiResponseAdminPaymentSet(
      Set<Payment> memberPaymentSet);

  SchedulePaymentResponse paymentToSchedulePaymentResponse(PaymentDto payment);
}
