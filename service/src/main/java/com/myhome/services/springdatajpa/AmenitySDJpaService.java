/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.AmenityDto;
import com.myhome.controllers.mapper.AmenityApiMapper;
import com.myhome.domain.Amenity;
import com.myhome.domain.Community;
import com.myhome.repositories.AmenityRepository;
import com.myhome.repositories.CommunityRepository;
import com.myhome.services.AmenityService;
import com.myhome.services.CommunityService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AmenitySDJpaService implements AmenityService {

  private final AmenityRepository amenityRepository;
  private final CommunityRepository communityRepository;
  private final CommunityService communityService;
  private final AmenityApiMapper amenityApiMapper;

  @Override
  public Optional<List<AmenityDto>> createAmenities(Set<AmenityDto> amenities, String communityId) {
    final Optional<Community> community = communityService.getCommunityDetailsById(communityId);
    if (!community.isPresent()) {
      return Optional.empty();
    }
    final List<Amenity> amenitiesWithCommunity = amenities.stream()
        .map(amenityApiMapper::amenityDtoToAmenity)
        .map(amenity -> {
          amenity.setCommunity(community.get());
          return amenity;
        })
        .collect(Collectors.toList());
    final List<AmenityDto> createdAmenities =
        amenityRepository.saveAll(amenitiesWithCommunity).stream()
            .map(amenityApiMapper::amenityToAmenityDto)
            .collect(Collectors.toList());
    return Optional.of(createdAmenities);
  }

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

  @Override
  public boolean updateAmenity(AmenityDto updatedAmenity) {
    String amenityId = updatedAmenity.getAmenityId();
    return amenityRepository.findByAmenityId(amenityId)
        .map(amenity -> communityRepository.findByCommunityId(updatedAmenity.getCommunityId())
            .map(community -> {
              Amenity updated = new Amenity();
              updated.setName(updatedAmenity.getName());
              updated.setPrice(updatedAmenity.getPrice());
              updated.setId(amenity.getId());
              updated.setAmenityId(amenityId);
              updated.setDescription(updatedAmenity.getDescription());
              return updated;
            })
            .orElse(null))
        .map(amenityRepository::save).isPresent();
  }
}
