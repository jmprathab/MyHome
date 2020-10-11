package com.myhome.controllers.mapper;

import com.myhome.controllers.response.GetAmenityBookingsResponse;
import com.myhome.domain.AmenityBookingItem;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface AmenityBookingItemApiMapper {

  List<GetAmenityBookingsResponse> amenityBookingToAmenityBookingsResponse(
      List<AmenityBookingItem> bookingItems);
}
