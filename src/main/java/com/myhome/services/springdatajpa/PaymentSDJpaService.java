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

package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.mapper.PaymentMapper;
import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.domain.Payment;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.repositories.PaymentRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.HouseMemberDocumentService;
import com.myhome.services.PaymentService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Implements {@link PaymentService} and uses Spring Data JPA Repository to do its work
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentSDJpaService implements PaymentService {
  private final PaymentRepository paymentRepository;
  private final UserRepository adminRepository;
  private final PaymentMapper paymentMapper;
  private final HouseMemberRepository houseMemberRepository;
  private final HouseMemberDocumentService houseMemberDocumentService;
  private final CommunityHouseRepository communityHouseRepository;
  private final CommunityRepository communityRepository;

  @Override
  public PaymentDto schedulePayment(PaymentDto request) {
    generatePaymentId(request);
    return createPaymentInRepository(request);
  }

  @Override
  public Set<Payment> listAll() {
    return listAll(200, 0);
  }

  @Override
  public Set<Payment> listAll(Integer limit, Integer start) {
    return paymentRepository.findAll(PageRequest.of(start, limit)).toSet();
  }

  @Override
  public Optional<PaymentDto> getPaymentDetails(String paymentId) {
    return paymentRepository.findByPaymentId(paymentId)
        .map(paymentMapper::paymentToPaymentDto);
  }

  @Override
  public Optional<HouseMember> getHouseMember(String memberId) {
    Optional<HouseMember> memberOptional = houseMemberRepository.findByMemberId(memberId);

    return memberOptional.map(member1 -> {
      HouseMember member = memberOptional.get();

      if (member.getHouseMemberDocument() == null) {
        HouseMemberDocument document = new HouseMemberDocument();
        document.setId(member.getId());
        member.setHouseMemberDocument(
            houseMemberDocumentService.findHouseMemberDocument(memberId).orElse(document));
      }

      return Optional.of(member);
    }).orElse(Optional.empty());
  }

  @Override
  public Set<Payment> getPaymentsByMember(String memberId) {
    ExampleMatcher ignoringMatcher = ExampleMatcher.matchingAll()
        .withMatcher("memberId",
            ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase())
        .withIgnorePaths("paymentId", "charge", "type", "description", "recurring", "dueDate",
            "adminId");

    Example<Payment> paymentExample =
        Example.of(new Payment(null, null, null, null, false, null, null, memberId),
            ignoringMatcher);

    return new HashSet<>(paymentRepository.findAll(paymentExample));
  }

  @Override
  public Set<Payment> getPaymentsByAdmin(String adminId) {
    ExampleMatcher ignoringMatcher = ExampleMatcher.matchingAll()
        .withMatcher("adminId",
            ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase())
        .withIgnorePaths("paymentId", "charge", "type", "description", "recurring", "dueDate",
            "memberId");

    Example<Payment> paymentExample =
        Example.of(new Payment(null, null, null, null, false, null, null, adminId),
            ignoringMatcher);

    return new HashSet<>(paymentRepository.findAll(paymentExample));
  }

  private PaymentDto createPaymentInRepository(PaymentDto request) {
    Payment payment = paymentMapper.paymentDtoToPayment(request);

    paymentRepository.save(payment);

    return paymentMapper.paymentToPaymentDto(payment);
  }

  private void generatePaymentId(PaymentDto request) {
    request.setPaymentId(UUID.randomUUID().toString());
  }
}
