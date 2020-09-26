package com.myhome.services;

import com.myhome.controllers.dto.AmenityDto;
import com.myhome.domain.Amenity;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AmenityService {

  Optional<List<AmenityDto>> createAmenities(Set<AmenityDto> amenities, String communityId);

  Optional<Amenity> getAmenityDetails(String amenityId);

  boolean deleteAmenity(String amenityId);

  Set<Amenity> listAllAmenities(String communityId);
}
