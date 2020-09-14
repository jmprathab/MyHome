package com.myhome.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCommunityAmenityRequest {
  private String description;
  private boolean isBooked;
  private String bookingStartDate;
  private String bookingEndDate;
  private String communityId;
}
