package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.AmenityDto;
import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import com.myhome.domain.Amenity;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface AmenityApiMapper {

  GetAmenityDetailsResponse amenityToAmenityDetailsResponse(Amenity amenity);

  Set<GetAmenityDetailsResponse> amenitiesSetToAmenityDetailsResponseSet(Set<Amenity> amenity);

  Amenity amenityDtoToAmenity(AmenityDto amenityDto);

  AmenityDto amenityToAmenityDto(Amenity amenity);
}
