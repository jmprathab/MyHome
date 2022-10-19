package com.myhome.repositories;

import com.myhome.domain.AmenityBookingItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AmenityBookingItemRepository extends JpaRepository<AmenityBookingItem, String> {
  Optional<AmenityBookingItem> findByAmenityBookingItemId(String amenityBookingItemId);

@Query("select b from AmenityBookingItem b where b.amenity.amenityId = :id " +
        "and  (( b.bookingStartDate >= :start  or b.bookingEndDate >= :start ) " +
        "and  (b.bookingStartDate <= :end  or b.bookingEndDate <= :end ))")
  List<AmenityBookingItem> findAllByAmenityIdAndTimeRangeBetween(@Param("id")String id, @Param("start")LocalDateTime start,
                                                                 @Param("end")LocalDateTime end, Pageable pageable);

  List<AmenityBookingItem> findAmenityBookingItemsByAmenity_AmenityId(String amenityId, Pageable pageable);
}
