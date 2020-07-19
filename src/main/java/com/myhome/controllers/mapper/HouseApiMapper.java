package com.myhome.controllers.mapper;

import com.myhome.controllers.response.GetHouseDetailsResponse;
import com.myhome.domain.CommunityHouse;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface HouseApiMapper {
  Set<GetHouseDetailsResponse.CommunityHouse> communityHouseSetToRestApiResponseCommunityHouseSet(
      Set<CommunityHouse> communityHouse);

  GetHouseDetailsResponse.CommunityHouse communityHouseToRestApiResponseCommunityHouse(
      CommunityHouse communityHouse);
}
