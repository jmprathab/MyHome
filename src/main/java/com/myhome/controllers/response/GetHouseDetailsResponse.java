package com.myhome.controllers.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetHouseDetailsResponse {
  private List<CommunityHouse> houses = new ArrayList<>();

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class CommunityHouse {
    private String houseId;
    private String name;
  }
}
