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

package com.myhome.services;

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import java.util.Optional;
import java.util.Set;

/**
 * Interface for service layer
 */
public interface PaymentService {
  PaymentDto schedulePayment(PaymentDto request);

  Set<Payment> listAll();

  Set<Payment> listAll(Integer limit, Integer start);

  Optional<PaymentDto> getPaymentDetails(String paymentId);

  Set<Payment> getPaymentsByMember(String memberId);

  Set<Payment> getPaymentsByAdmin(String adminId);

  Optional<HouseMember> getHouseMember(String memberId);
}
