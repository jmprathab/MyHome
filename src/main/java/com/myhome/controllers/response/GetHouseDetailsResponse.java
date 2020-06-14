package com.myhome.controllers.response;

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
  private Set<CommunityHouse> houses;

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class CommunityHouse {
    private String houseId;
    private String name;
  }
}
