package com.myhome.services;

import com.myhome.domain.Amenity;
import com.myhome.domain.AmenityBookingItem;

import java.time.LocalDateTime;

public interface AmenityBookingService {
  AmenityBookingItem bookAmenity(Amenity amenity, LocalDateTime bookingStartDate, LocalDateTime bookingEndDate);

  boolean deleteBooking(String bookingId);

  boolean removeAllAmenityBookings(String amenityID);
}
