package com.myhome.services;

import com.myhome.domain.CommunityAmenity;

import java.util.Optional;
import java.util.Set;

public interface CommunityAmenityService {
  Optional<CommunityAmenity> getCommunityAmenityDetails(String amenityId);

  boolean deleteAmenity(String amenityId);

  Set<CommunityAmenity> listAllCommunityAmenities(String communityId);
}
