package com.myhome.services.springdatajpa;

import com.myhome.domain.AmenityBookingItem;
import com.myhome.repositories.AmenityBookingItemRepository;
import com.myhome.services.BookingService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingSDJpaService implements BookingService {

  private final AmenityBookingItemRepository bookingRepository;

  @Transactional
  @Override
  public boolean deleteBooking(String amenityId, String bookingId) {
    Optional<AmenityBookingItem> booking =
        bookingRepository.findByAmenityBookingItemId(bookingId);
    return booking.map(bookingItem -> {
      boolean amenityFound =
          bookingItem.getAmenity().getAmenityId().equals(amenityId);
      if (amenityFound) {
        bookingRepository.delete(bookingItem);
        return true;
      } else {
        return false;
      }
    }).orElse(false);
  }

  @Override
  public Set<AmenityBookingItem> getAllBookingForAmenityBetween(String amenityId, LocalDateTime start, LocalDateTime end,
                                                                Pageable pageable) {
    return new HashSet<>(bookingRepository.findAllByAmenityIdAndTimeRangeBetween(amenityId, start, end, pageable));
  }

  @Override
  public Set<AmenityBookingItem> getAllBookingForAmenity(String amenityId, Pageable pageable) {
    return new HashSet<>(bookingRepository.findAmenityBookingItemsByAmenity_AmenityId(amenityId, pageable));
  }

}
