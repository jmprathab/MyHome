package com.myhome.controllers.mapper;

import com.myhome.controllers.response.amenity.GetCommunityAmenityDetailsResponse;
import com.myhome.domain.CommunityAmenity;
import org.mapstruct.Mapper;

@Mapper
public interface CommunityAmenityApiMapper {

  GetCommunityAmenityDetailsResponse communityAmenityToCommunityAmenityDetailsResponse(CommunityAmenity communityAmenity);

}
