package com.myhome.services.unit;

import com.myhome.controllers.dto.PaymentDto;
import com.myhome.controllers.dto.UserDto;
import com.myhome.controllers.dto.mapper.PaymentMapper;
import com.myhome.domain.HouseMember;
import com.myhome.domain.Payment;
import com.myhome.domain.User;
import com.myhome.model.HouseMemberDto;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.repositories.PaymentRepository;
import com.myhome.repositories.UserRepository;
import com.myhome.services.springdatajpa.PaymentSDJpaService;
import helpers.TestUtils;
import io.jsonwebtoken.lang.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PaymentSDJpaServiceTest {

  private final BigDecimal TEST_PAYMENT_CHARGE = new BigDecimal(1000);
  private final String TEST_PAYMENT_TYPE = "test-type";
  private final String TEST_PAYMENT_DESCRIPTION = "test-description";
  private final boolean TEST_PAYMENT_RECURRING = true;
  private final LocalDate TEST_PAYMENT_DUEDATE = LocalDate.now();
  private final UserDto TEST_PAYMENT_USER = null; //this package is private/inaccessible
  private final HouseMemberDto TEST_PAYMENT_MEMBER = new HouseMemberDto();

  @Mock
  private PaymentRepository paymentRepository;
  @Mock
  private UserRepository adminRepository;
  @Mock
  private PaymentMapper paymentMapper;
  @Mock
  private HouseMemberRepository houseMemberRepository;
  @Captor
  ArgumentCaptor<Example> exampleCaptor;

  @InjectMocks
  private PaymentSDJpaService paymentSDJpaService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void schedulePayment() {
    //given
    PaymentDto testPaymentDto = TestUtils.PaymentHelpers.getTestPaymentDto(TEST_PAYMENT_CHARGE,TEST_PAYMENT_TYPE,TEST_PAYMENT_DESCRIPTION,TEST_PAYMENT_RECURRING,TEST_PAYMENT_DUEDATE,TEST_PAYMENT_USER,TEST_PAYMENT_MEMBER);
    Payment basePayment = new Payment();

    given(paymentMapper.paymentDtoToPayment(any(PaymentDto.class))).willReturn(basePayment);
    given(paymentMapper.paymentToPaymentDto(any(Payment.class))).willReturn(testPaymentDto);

    //when
    PaymentDto paymentScheduled = paymentSDJpaService.schedulePayment(testPaymentDto);

    //then
    verify(adminRepository).save(any()); //User gets associated with payment
    verify(paymentRepository).save(any(Payment.class)); //Payment is persisted
    Assert.notNull(paymentScheduled.getPaymentId()); //generation of payment ID
  }

  @Test
  void getPaymentDetails() {
    //when
    PaymentDto testPaymentDto = TestUtils.PaymentHelpers.getTestPaymentDto(TEST_PAYMENT_CHARGE,TEST_PAYMENT_TYPE,TEST_PAYMENT_DESCRIPTION,TEST_PAYMENT_RECURRING,TEST_PAYMENT_DUEDATE,TEST_PAYMENT_USER,TEST_PAYMENT_MEMBER);
    Payment basePayment = new Payment();

    given(paymentRepository.findByPaymentId(anyString())).willReturn(Optional.of(basePayment));
    given(paymentMapper.paymentToPaymentDto(any(Payment.class))).willReturn(testPaymentDto);

    //when
    Optional<PaymentDto> paymentDetails = paymentSDJpaService.getPaymentDetails("any-id");

    //then
    verify(paymentRepository).findByPaymentId(anyString()); //fetching data
    assertTrue(paymentDetails.isPresent()); //element is present
  }

  @Test
  void getHouseMember() {
    //given
    HouseMember houseMember = TestUtils.HouseMemberHelpers.getTestHouseMember();

    given(houseMemberRepository.findByMemberId(anyString())).willReturn(
        Optional.of(houseMember));

    //when
    Optional<HouseMember> houseMemberOptional = paymentSDJpaService.getHouseMember("any-id");

    //then
    verify(houseMemberRepository).findByMemberId(anyString()); //fetching data
    assertTrue(houseMemberOptional.isPresent()); //element is present
  }

  @Test
  void getPaymentsByMember() {
    //given
    String memberId1 = "memberId-test-1";
    String memberId2 = "memberId-test-2";
    Payment paymentExample1 = TestUtils.PaymentHelpers.getTestPaymentNullFields();
    paymentExample1.setMember(new HouseMember().withMemberId(memberId1));
    Payment paymentExample2 = TestUtils.PaymentHelpers.getTestPaymentNullFields();
    paymentExample2.setMember(new HouseMember().withMemberId(memberId2));

    //when
    Set<Payment> paymentExecution1 = paymentSDJpaService.getPaymentsByMember(memberId1);
    verify(paymentRepository).findAll(exampleCaptor.capture()); //verify and capture first execution
    Example<Payment> capturedParameter1 = exampleCaptor.getValue(); //Capturing the 'paymentExample' created by the method
    Payment capturedPaymentExample1 = capturedParameter1.getProbe();

    Set<Payment> paymentExecution2 = paymentSDJpaService.getPaymentsByMember(memberId2);
    verify(paymentRepository,times(2)).findAll(exampleCaptor.capture()); //verify and capture second execution
    Example<Payment> capturedParameter2 = exampleCaptor.getValue(); // Capturing the 'paymentExample' created by the method
    Payment capturedPaymentExample2 = capturedParameter2.getProbe();

    //then
    verify(paymentRepository,times(2)).findAll(any(Example.class)); //two executions of method
    assertEquals(memberId1,capturedPaymentExample1.getMember().getMemberId()); //memberId from captured element is the same passed on as parameter in method
    assertEquals(memberId2,capturedPaymentExample2.getMember().getMemberId()); //memberId from captured element is the same passed on as parameter in method
    assertEquals(paymentExample1,capturedPaymentExample1); //fields in captured element should be as expected
    assertEquals(paymentExample2,capturedPaymentExample2); //fields in captured element should be as expected
  }

  @Test
  void getPaymentsByAdmin() {
    //given
    String userId1 = "userId-test-1";
    String userId2 = "userId-test-2";
    Payment paymentExample1 = TestUtils.PaymentHelpers.getTestPaymentNullFields();
    paymentExample1.setAdmin(new User().withUserId(userId1));
    Payment paymentExample2 = TestUtils.PaymentHelpers.getTestPaymentNullFields();
    paymentExample2.setAdmin(new User().withUserId(userId2));

    Pageable pageable = Mockito.mock(Pageable.class);

    //when
    Page<Payment> paymentExecution1 = paymentSDJpaService.getPaymentsByAdmin(userId1,pageable);
    verify(paymentRepository).findAll((Example<Payment>) exampleCaptor.capture(), any(Pageable.class)); //verify and capture first execution
    Example<Payment> capturedParameter1 = exampleCaptor.getValue(); //Capturing the 'paymentExample' created by method
    Payment capturedPaymentExample1 = capturedParameter1.getProbe();

    Page<Payment> paymentExecution2 = paymentSDJpaService.getPaymentsByAdmin(userId2,pageable);
    verify(paymentRepository,times(2)).findAll((Example<Payment>) exampleCaptor.capture(), any(Pageable.class)); //verify and capture first execution
    Example<Payment> capturedParameter2 = exampleCaptor.getValue(); // Capturing the 'paymentExample' created by method
    Payment capturedPaymentExample2 = capturedParameter2.getProbe();

    //then
    verify(paymentRepository,times(2)).findAll(any(Example.class),any(Pageable.class)); //assert two executions of method
    assertEquals(userId1,capturedPaymentExample1.getAdmin().getUserId()); //userId from captured element is the same passed on as parameter in method
    assertEquals(userId2,capturedPaymentExample2.getAdmin().getUserId()); //userId from captured element is the same passed on as parameter in method
    assertEquals(paymentExample1,capturedPaymentExample1); //fields in captured element should be as expected
    assertEquals(paymentExample2,capturedPaymentExample2); //fields in captured element should be as expected
  }
}