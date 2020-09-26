package com.myhome.services;

import com.myhome.domain.Amenity;

import java.util.Optional;
import java.util.Set;

public interface AmenityService {
  Optional<Amenity> getAmenityDetails(String amenityId);

  boolean deleteAmenity(String amenityId);

  Set<Amenity> listAllAmenities(String communityId);
}
