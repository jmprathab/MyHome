package com.myhome.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetAmenityBookingsResponse {
  private String amenityId;
  private String bookingStartDate;
  private String bookingEndDate;
}
