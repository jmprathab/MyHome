package com.myhome.services.springdatajpa;

import com.myhome.domain.Community;
import com.myhome.domain.Amenity;
import com.myhome.repositories.AmenityRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.services.AmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AmenitySDJpaService implements AmenityService {

  private final AmenityRepository amenityRepository;
  private final CommunityRepository communityRepository;

  @Override
  public Optional<Amenity> getAmenityDetails(String amenityId) {
    return amenityRepository.findByAmenityId(amenityId);
  }

  @Override
  public boolean deleteAmenity(String amenityId) {
    return amenityRepository.findByAmenityIdWithCommunity(amenityId)
        .map(amenity -> {
          Community community = amenity.getCommunity();
          community.getAmenities().remove(amenity);
          amenityRepository.delete(amenity);
          return true;
        })
        .orElse(false);
  }

  @Override
  public Set<Amenity> listAllAmenities(String communityId) {
    return communityRepository.findByCommunityIdWithAmenities(communityId)
        .map(Community::getAmenities)
        .orElse(new HashSet<>());
  }
}
