package com.myhome.services.springdatajpa;

import com.myhome.domain.Community;
import com.myhome.domain.CommunityAmenity;
import com.myhome.repositories.CommunityAmenityRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.services.CommunityAmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommunityAmenitySDJpaService implements CommunityAmenityService {

  private final CommunityAmenityRepository communityAmenityRepository;
  private final CommunityRepository communityRepository;

  @Override
  public Optional<CommunityAmenity> getCommunityAmenityDetails(String amenityId) {
    return communityAmenityRepository.findByAmenityId(amenityId);
  }

  @Override
  public boolean deleteAmenity(String amenityId) {
    return communityAmenityRepository.findByAmenityIdWithCommunity(amenityId)
        .map(amenity -> {
          Community community = amenity.getCommunity();
          community.getAmenities().remove(amenity);
          communityAmenityRepository.delete(amenity);
          return true;
        })
        .orElse(false);
  }

  @Override
  public Set<CommunityAmenity> listAllCommunityAmenities(String communityId) {
    Optional<Community> communityOptional = communityRepository.findByCommunityId(communityId);
    return communityOptional
        .map(community -> community.getAmenities())
        .orElse(new HashSet<>());

  }

}
