package com.myhome.services.springdatajpa;

import com.myhome.domain.Amenity;
import com.myhome.domain.AmenityBookingItem;
import com.myhome.repositories.AmenityBookingItemRepository;
import com.myhome.services.AmenityBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AmenityBookingSDJpaService implements AmenityBookingService {

  private final AmenityBookingItemRepository bookingItemRepository;

  @Override
  public AmenityBookingItem bookAmenity(Amenity amenity, LocalDateTime bookingStartDate, LocalDateTime bookingEndDate) {
    AmenityBookingItem newBooking = new AmenityBookingItem(null, amenity,
        bookingStartDate, bookingEndDate, null);
    return bookingItemRepository.save(newBooking);
  }

  @Override
  public boolean deleteBooking(String bookingId) {
    return bookingItemRepository.findByAmenityBookingItemId(bookingId)
        .map(bookingItem -> {
          bookingItemRepository.delete(bookingItem);
          return true;
        })
        .orElse(false);
  }

  @Override
  public void removeAllAmenityBookings(String amenityId) {
    HashSet<AmenityBookingItem> bookings = bookingItemRepository.findAllByAmenity_AmenityId(amenityId);
    if(!bookings.isEmpty()) {
      bookingItemRepository.deleteAll(bookings);
    }
  }
}
