package com.myhome.controllers.response;

import com.myhome.controllers.response.amenity.GetAmenityDetailsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetAmenityBookingsResponse {
  private GetAmenityDetailsResponse amenity;
  private String bookingStartDate;
  private String bookingEndDate;
}
