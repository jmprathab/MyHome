package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.CommunityAmenityDto;
import com.myhome.domain.Community;
import com.myhome.domain.CommunityAmenity;
import com.myhome.repositories.CommunityAmenityRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.services.CommunityAmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    return communityRepository.findByCommunityId(communityId)
    .map(Community::getAmenities)
    .orElse(new HashSet<>());
  }
  
  @Override
  public boolean updateAmenity(CommunityAmenityDto updatedCommunityAmenity) {
    String amenityId = updatedCommunityAmenity.getAmenityId();
    return communityAmenityRepository.findByAmenityId(amenityId)
      .map(communityAmenity -> communityRepository.findByCommunityId(updatedCommunityAmenity.getCommunityId())
        .map(community -> {
          CommunityAmenity updated = new CommunityAmenity();
          updated.setId(communityAmenity.getId());
          updated.setAmenityId(amenityId);
          updated.setDescription(updatedCommunityAmenity.getDescription());
          updated.setBooked(updatedCommunityAmenity.isBooked());
          updated.setBookingStartDate(LocalDateTime.parse(
                updatedCommunityAmenity.getBookingStartDate(),
                DateTimeFormatter.ofPattern(CommunityAmenity.BOOKING_DATE_TIME_FORMAT)));
          updated.setBookingEndDate(LocalDateTime.parse(
                updatedCommunityAmenity.getBookingEndDate(),
                DateTimeFormatter.ofPattern(CommunityAmenity.BOOKING_DATE_TIME_FORMAT)));

          return updated;
        })
        .orElse(null))
      .map(communityAmenityRepository::save).isPresent();
  }
}
