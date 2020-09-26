package com.myhome.controllers.request;

import com.myhome.controllers.dto.AmenityDto;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddAmenityRequest {
  private Set<AmenityDto> amenities = new HashSet<>();
}
