package com.myhome.repositories;

import com.myhome.domain.Amenity;
import com.myhome.domain.AmenityBookingItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AmenityBookingItemRepository extends JpaRepository<AmenityBookingItem, String> {
    Optional<AmenityBookingItem> findByAmenityBookingItemId(String amenityBookingItemId);
}
