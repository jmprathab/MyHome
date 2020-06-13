package com.myhome.controllers.request;

import com.myhome.controllers.dto.HouseMemberDto;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddHouseMemberRequest {
  @NotNull
  private HouseMemberDto member;
}
