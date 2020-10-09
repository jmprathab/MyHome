package com.myhome.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetAmenityBookingsResponse {
    String amenityId;
    String bookingStartDate;
    String bookingEndDate;
}
