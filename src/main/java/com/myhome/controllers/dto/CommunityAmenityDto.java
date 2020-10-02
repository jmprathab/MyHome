package com.myhome.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommunityAmenityDto {
  private Long id;
  private String amenityId;
  private String description;
  private boolean isBooked;
  private String bookingStartDate;
  private String bookingEndDate;
  private String communityId;
}
