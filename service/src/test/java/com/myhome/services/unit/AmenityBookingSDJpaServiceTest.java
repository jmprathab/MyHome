package com.myhome.services.unit;

import com.myhome.domain.AmenityBookingItem;
import com.myhome.repositories.AmenityBookingItemRepository;
import com.myhome.services.AmenityBookingService;
import com.myhome.services.springdatajpa.AmenityBookingSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class AmenityBookingSDJpaServiceTest {

  private static final String TEST_BOOKING_ID = "test-booking-id";
  @Mock
  private AmenityBookingItemRepository bookingItemRepository;

  @InjectMocks
  private AmenityBookingSDJpaService amenityBookingService;

  @BeforeEach
  private void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void deleteBookingItem() {
    // given
    AmenityBookingItem testBookingItem = getTestBookingItem();

    given(bookingItemRepository.findByAmenityBookingItemId(TEST_BOOKING_ID))
        .willReturn(Optional.of(testBookingItem));

    // when
    boolean bookingDeleted = amenityBookingService.deleteBooking(TEST_BOOKING_ID);

    // then
    assertTrue(bookingDeleted);
    verify(bookingItemRepository).findByAmenityBookingItemId(TEST_BOOKING_ID);
    verify(bookingItemRepository).delete(testBookingItem);
  }

  @Test
  void deleteBookingNotExists() {
    // given
    given(bookingItemRepository.findByAmenityBookingItemId(TEST_BOOKING_ID))
        .willReturn(Optional.empty());

    // when
    boolean amenityDeleted = amenityBookingService.deleteBooking(TEST_BOOKING_ID);

    // then
    assertFalse(amenityDeleted);
    verify(bookingItemRepository).findByAmenityBookingItemId(TEST_BOOKING_ID);
    verify(bookingItemRepository, never()).delete(any());
  }

  private AmenityBookingItem getTestBookingItem() {
    return new AmenityBookingItem()
        .withAmenityBookingItemId(TEST_BOOKING_ID);
  }

}