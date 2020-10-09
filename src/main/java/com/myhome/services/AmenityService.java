package com.myhome.services;

import com.myhome.controllers.dto.AmenityDto;
import com.myhome.domain.Amenity;
import com.myhome.domain.AmenityBookingItem;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AmenityService {

  Optional<List<AmenityDto>> createAmenities(Set<AmenityDto> amenities, String communityId);

  Optional<Amenity> getAmenityDetails(String amenityId);

  boolean deleteAmenity(String amenityId);

  Set<Amenity> listAllAmenities(String communityId);

  List<AmenityBookingItem> listAllAmenityBookings(String amenityId,
                                                  LocalDateTime startDate,
                                                  LocalDateTime endDate,
                                                  Pageable pageable);
}
