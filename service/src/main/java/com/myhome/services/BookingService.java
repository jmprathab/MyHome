package com.myhome.services;

import com.myhome.domain.AmenityBookingItem;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Set;

public interface BookingService {

  boolean deleteBooking(String amenityId, String bookingId);

  Set<AmenityBookingItem> getAllBookingForAmenityBetween(String amenityId, LocalDateTime start, LocalDateTime end,
                                                         Pageable pageable);
  Set<AmenityBookingItem> getAllBookingForAmenity(String amenityId, Pageable pageable);
}
