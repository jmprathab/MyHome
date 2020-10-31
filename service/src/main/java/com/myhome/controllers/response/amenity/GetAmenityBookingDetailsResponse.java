package com.myhome.controllers.response.amenity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GetAmenityBookingDetailsResponse {
  private String amenityId;
  private String amenityBookingId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String bookingUserId;
}
