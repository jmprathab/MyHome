package com.myhome.controllers.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AmenityBookingDto {

  private Long id;
  private AmenityDto amenity;
  private UserDto user;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

}
