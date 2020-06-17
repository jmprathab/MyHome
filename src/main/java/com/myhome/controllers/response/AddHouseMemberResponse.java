package com.myhome.controllers.response;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddHouseMemberResponse {
  private Set<HouseMember> members = new HashSet<>();

  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  @Setter
  static public class HouseMember {
    private String memberId;
    private String name;
  }
}
