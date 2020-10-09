package com.myhome.controllers.mapper;

import com.myhome.controllers.response.GetAmenityBookingsResponse;
import com.myhome.domain.AmenityBookingItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AmenityBookingItemApiMapper {

    List<GetAmenityBookingsResponse> amenityBookingToAmenityBookingsResponse(List<AmenityBookingItem> bookingItems);
}
