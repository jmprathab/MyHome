package com.myhome.repositories;

import com.myhome.domain.AmenityBookingItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AmenityBookingItemRepository extends JpaRepository<AmenityBookingItem, String>,
    JpaSpecificationExecutor<AmenityBookingItem> {
  Optional<AmenityBookingItem> findByAmenityBookingItemId(String amenityBookingItemId);
}
