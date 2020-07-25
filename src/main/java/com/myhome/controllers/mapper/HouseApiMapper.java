package com.myhome.controllers.mapper;

import com.myhome.controllers.response.GetHouseDetailsResponse;
import com.myhome.domain.CommunityHouse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface HouseApiMapper {
  List<GetHouseDetailsResponse.CommunityHouse> communityHouseSetToRestApiResponseCommunityHouseSet(
      List<CommunityHouse> communityHouse);

  GetHouseDetailsResponse.CommunityHouse communityHouseToRestApiResponseCommunityHouse(
      CommunityHouse communityHouse);
}
