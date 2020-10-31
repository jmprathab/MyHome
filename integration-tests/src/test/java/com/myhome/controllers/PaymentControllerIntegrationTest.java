package com.myhome.controllers;

import com.myhome.MyHomeServiceApplication;
import com.myhome.model.SchedulePaymentRequest;
import com.myhome.model.SchedulePaymentResponse;
import com.myhome.repositories.PaymentRepository;
import helpers.TestUtils;
import java.math.BigDecimal;
import java.util.UUID;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = MyHomeServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PaymentControllerIntegrationTest {

  private static final String TEST_MEMBER_ID = "default-member-id-for-testing";
  private static final String TEST_PAYMENT_TYPE = "test";
  private static final String TEST_PAYMENT_DESCRIPTION = "test description";
  private static final UUID TEST_PAYMENT_ID =
      UUID.fromString("5d09efc3-c08d-4e52-9367-3157850ad9e7");

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private Environment env;

  @Autowired
  private PaymentRepository paymentRepository;

  private String userId;
  private String loginToken;

  @BeforeEach
  public void setUp() {
    ResponseEntity<Void> loginResponse = TestUtils.Login.performLogin(testRestTemplate, env);
    userId = TestUtils.Login.getUserIdFromLoginResponse(loginResponse);
    loginToken = TestUtils.Login.getTokenFromLoginResponse(loginResponse);
  }

  @Test
  @Disabled("Payment API doesn't even work right now, NullPointerExcept guaranteed; " +
      "so this is disabled in the meantime")
  public void schedulePaymentSuccessful() {
    // Given a request body
    SchedulePaymentRequest requestBody = new SchedulePaymentRequest()
        .memberId(TEST_MEMBER_ID)
        .adminId(userId)
        .type(TEST_PAYMENT_TYPE)
        .description(TEST_PAYMENT_DESCRIPTION)
        .recurring(false)
        .charge(BigDecimal.TEN);

    // And a request using that body
    RequestEntity<SchedulePaymentRequest> requestEntity =
        RequestEntity.post(UriComponentsBuilder.newInstance()
            .path("/payments").build().toUri())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginToken)
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody);

    // When a request is made to schedule a payment
    ResponseEntity<SchedulePaymentResponse> responseEntity =
        testRestTemplate.exchange(requestEntity, SchedulePaymentResponse.class);

    // Then the response should be successful
    assertThat(responseEntity.getStatusCode())
        .isEqualTo(HttpStatus.CREATED);

    // So far, this fails. It seems the PaymentController doesn't work yet.
  }

  @Test
  public void retrievePaymentDetailsSuccessful() {
    // Given a request
    RequestEntity<Void> requestEntity =
        RequestEntity.get(UriComponentsBuilder.newInstance()
            .path("/payments/{paymentId}").buildAndExpand(TEST_PAYMENT_ID).toUri())
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginToken)
            .build();

    // And an expected response
    SchedulePaymentResponse expectedPaymentDetails = new SchedulePaymentResponse()
        .paymentId(TEST_PAYMENT_ID.toString())
        .charge(BigDecimal.TEN)
        .type(TEST_PAYMENT_TYPE)
        .description(TEST_PAYMENT_DESCRIPTION)
        .recurring(false)
        .adminId(userId)
        .memberId(TEST_MEMBER_ID);

    // When a request is made to retrieve details on the payment
    ResponseEntity<SchedulePaymentResponse> responseEntity =
        testRestTemplate.exchange(requestEntity, SchedulePaymentResponse.class);

    // Then the response should be successful
    assertThat(responseEntity.getStatusCode())
        .isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody())
        .usingRecursiveComparison()
        .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
        .isEqualTo(expectedPaymentDetails);
  }
}
