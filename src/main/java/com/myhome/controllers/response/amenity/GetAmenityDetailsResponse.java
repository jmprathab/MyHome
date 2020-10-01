package com.myhome.controllers.response.amenity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetAmenityDetailsResponse {
  private String amenityId;
  private String description;
}
