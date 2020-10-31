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

package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.AmenityDto;
import com.myhome.controllers.request.UpdateAmenityRequest;
import com.myhome.controllers.response.amenity.GetAmenityBookingDetailsResponse;
import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import com.myhome.domain.Amenity;
import com.myhome.domain.AmenityBookingItem;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface AmenityApiMapper {

  GetAmenityDetailsResponse amenityToAmenityDetailsResponse(Amenity amenity);

  Set<GetAmenityDetailsResponse> amenitiesSetToAmenityDetailsResponseSet(Set<Amenity> amenity);

  Amenity amenityDtoToAmenity(AmenityDto amenityDto);

  AmenityDto amenityToAmenityDto(Amenity amenity);

  AmenityDto updateAmenityRequestToAmenityDto(UpdateAmenityRequest request);

  @Mappings({
      @Mapping(target = "amenityId", source = "amenity.amenityId"),
      @Mapping(target = "amenityBookingId", source = "amenityBookingItemId"),
      @Mapping(target = "startDate", source = "bookingStartDate"),
      @Mapping(target = "endDate", source = "bookingEndDate"),
      @Mapping(target = "bookingUserId", source = "bookingUser.userId")
  })
  GetAmenityBookingDetailsResponse amenityBookingToAmenityBookingsDetailsResponse(AmenityBookingItem amenityBooking);

  Set<GetAmenityBookingDetailsResponse> amenityBookingsSetToAmenityBookingDetailsResponseSet(
      Set<AmenityBookingItem> amenityBookings);
}
