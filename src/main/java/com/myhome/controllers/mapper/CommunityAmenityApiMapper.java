package com.myhome.controllers.mapper;

import com.myhome.controllers.response.amenity.GetCommunityAmenityDetailsResponse;
import com.myhome.domain.CommunityAmenity;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface CommunityAmenityApiMapper {

  GetCommunityAmenityDetailsResponse communityAmenityToCommunityAmenityDetailsResponse(CommunityAmenity communityAmenity);

  Set<GetCommunityAmenityDetailsResponse> communityAmenitiesSetToCommunityAmenityDetailsResponseSet(Set<CommunityAmenity> communityAmenity);
}
