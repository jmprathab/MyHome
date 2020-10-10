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
package com.myhome.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity identifying a payment in the service. This could be an electricity bill, house rent, water
 * charge etc
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Payment extends BaseEntity {
  @Column(unique = true, nullable = false)
  private String paymentId;
  @Column(nullable = false)
  private BigDecimal charge;
  @Column(nullable = false)
  private String type;
  @Column(unique = true, nullable = false)
  private String description;
  @Column(nullable = false)
  private boolean recurring;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dueDate;
  private User admin;
  private HouseMember member;
}
