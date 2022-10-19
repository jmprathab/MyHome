package com.myhome.controllers.mapper;

import com.myhome.domain.AmenityBookingItem;
import com.myhome.model.GetBookingDetailsResponse;
import org.mapstruct.Mapper;

@Mapper
public interface BookingApiMapper {

    GetBookingDetailsResponse bookingToBookingDetailsResponse(AmenityBookingItem item);

}
