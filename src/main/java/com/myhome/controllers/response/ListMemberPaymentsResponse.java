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

package com.myhome.controllers.response;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListMemberPaymentsResponse {
  private Set<MemberPayment> payments = new HashSet<>();

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MemberPayment {
    private String memberId;
    private String paymentId;
    private BigDecimal charge;
    private String dueDate;

    /*This just displays the bare minimum as it is listing responses of existing payments. So if you want to find
    more payment details just hit the /members/payments/{paymentId} endpoint with the paymentId provided*/
  }
}
