package com.myhome.controllers.dto.mapper;

import com.myhome.controllers.dto.HouseHistoryDto;
import com.myhome.controllers.dto.HouseMemberDto;
import com.myhome.controllers.response.AddHouseMemberResponse;
import com.myhome.controllers.response.ListHouseMembersResponse;
import com.myhome.domain.HouseHistory;
import com.myhome.domain.HouseMember;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface HouseMemberMapper {
  Set<ListHouseMembersResponse.HouseMember> houseMemberSetToRestApiResponseHouseMemberSet(
      Set<HouseMember> houseMemberSet);

  Set<HouseMember> houseMemberDtoSetToHouseMemberSet(Set<HouseMemberDto> houseMemberDtoSet);

  Set<AddHouseMemberResponse.HouseMember> houseMemberSetToRestApiResponseAddHouseMemberSet(
      Set<HouseMember> houseMemberSet);

  HouseHistory houseHistoryDtoToHouseHistory(HouseHistoryDto houseHistoryDto);

  HouseHistoryDto houseHistoryPoToHouseHistoryDto(HouseHistory houseHistory);
}
