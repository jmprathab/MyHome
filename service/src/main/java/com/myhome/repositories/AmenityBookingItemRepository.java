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

    @Query("select ab from AmenityBookingItem ab " +
        "left join fetch ab.amenity " +
        "where ab.amenity.amenityId = :amenityId " +
        "and (ab.bookingStartDate >= :startDate or :startDate is null) " +
        "and (ab.bookingEndDate <= :endDate or :endDate is null)")
    List<AmenityBookingItem> findAllByAmenity(@Param("amenityId") String amenityId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable);
}
