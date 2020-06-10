package com.prathab.communityservice.controllers.models.response;

import com.prathab.communityservice.controllers.dto.CommunityHouseDto;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetHouseDetailsResponse {
  private Set<CommunityHouseDto> houses;
}
