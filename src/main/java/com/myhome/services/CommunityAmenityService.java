package com.myhome.services;

import com.myhome.domain.CommunityAmenity;

import java.util.Optional;

public interface CommunityAmenityService {
  Optional<CommunityAmenity> getCommunityAmenityDetails(String amenityId);

  boolean deleteAmenity(String amenityId);
}
