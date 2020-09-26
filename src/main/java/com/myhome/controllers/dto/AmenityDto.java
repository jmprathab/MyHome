package com.myhome.controllers.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AmenityDto {
  private Long id;
  private String amenityId;
  private String name;
  private String description;
  private BigDecimal price;
  private String communityId;
}
