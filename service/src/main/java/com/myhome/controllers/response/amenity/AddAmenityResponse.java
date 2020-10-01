package com.myhome.controllers.response.amenity;

import com.myhome.controllers.dto.AmenityDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddAmenityResponse {
  private List<AmenityDto> amenities;
}
