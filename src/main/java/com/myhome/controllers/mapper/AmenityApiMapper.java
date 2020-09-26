package com.myhome.controllers.mapper;

import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import com.myhome.domain.Amenity;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface AmenityApiMapper {

  GetAmenityDetailsResponse amenityToAmenityDetailsResponse(Amenity amenity);

  Set<GetAmenityDetailsResponse> amenitiesSetToAmenityDetailsResponseSet(Set<Amenity> amenity);
}
