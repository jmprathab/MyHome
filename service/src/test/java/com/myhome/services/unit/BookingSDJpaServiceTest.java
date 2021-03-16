package com.myhome.services.unit;

import com.myhome.domain.AmenityBookingItem;
import com.myhome.repositories.AmenityBookingItemRepository;
import com.myhome.services.springdatajpa.BookingSDJpaService;
import helpers.TestUtils;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BookingSDJpaServiceTest {

  private static final String TEST_BOOKING_ID = "test-booking-id";
  private static final String TEST_AMENITY_ID = "test-amenity-id";
  private static final String TEST_AMENITY_ID_2 = "test-amenity-id-2";
  private final String TEST_AMENITY_DESCRIPTION = "test-amenity-description";

  @Mock
  private AmenityBookingItemRepository bookingItemRepository;

  @InjectMocks
  private BookingSDJpaService bookingSDJpaService;

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
    testBookingItem.setAmenity(TestUtils.AmenityHelpers
        .getTestAmenity(TEST_AMENITY_ID, TEST_AMENITY_DESCRIPTION));

    // when
    boolean bookingDeleted = bookingSDJpaService.deleteBooking(TEST_AMENITY_ID, TEST_BOOKING_ID);

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
    boolean bookingDeleted = bookingSDJpaService.deleteBooking(TEST_AMENITY_ID, TEST_BOOKING_ID);

    // then
    assertFalse(bookingDeleted);
    verify(bookingItemRepository).findByAmenityBookingItemId(TEST_BOOKING_ID);
    verify(bookingItemRepository, never()).delete(any());
  }

  @Test
  void deleteBookingAmenityNotExists() {
    // given
    AmenityBookingItem testBookingItem = getTestBookingItem();

    given(bookingItemRepository.findByAmenityBookingItemId(TEST_BOOKING_ID))
        .willReturn(Optional.of(testBookingItem));
    testBookingItem.setAmenity(TestUtils.AmenityHelpers
        .getTestAmenity(TEST_AMENITY_ID_2, TEST_AMENITY_DESCRIPTION));
    // when
    boolean bookingDeleted = bookingSDJpaService.deleteBooking(TEST_AMENITY_ID, TEST_BOOKING_ID);

    // then
    assertFalse(bookingDeleted);
    assertNotEquals(TEST_AMENITY_ID, testBookingItem.getAmenity().getAmenityId());
    verify(bookingItemRepository).findByAmenityBookingItemId(TEST_BOOKING_ID);
    verify(bookingItemRepository, never()).delete(any());
  }

  private AmenityBookingItem getTestBookingItem() {
    return new AmenityBookingItem()
        .withAmenityBookingItemId(TEST_BOOKING_ID);
  }
}
