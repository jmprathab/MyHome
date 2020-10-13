package com.myhome.controllers.dto.mapper;

import com.myhome.controllers.dto.HouseHistoryDto;
import com.myhome.domain.HouseHistory;
import org.mapstruct.Mapper;

@Mapper
public interface HouseHistoryMapper {
  HouseHistory HouseHistoryDtoToHouseHistory(HouseHistoryDto houseHistoryDto);
  HouseHistoryDto HouseHistoryToHouseHistoryDto(HouseHistory houseHistory);
}
