package com.myhome.services.springdatajpa;

import com.myhome.repositories.AmenityBookingItemRepository;
import com.myhome.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingSDJpaService implements BookingService {

  private final AmenityBookingItemRepository bookingRepository;

  @Override
  public boolean deleteBooking(String bookingId) {
    return bookingRepository.findByAmenityBookingItemId(bookingId)
        .map(bookingItem -> {
          bookingRepository.delete(bookingItem);
          return true;
        })
        .orElse(false);
  }
}
