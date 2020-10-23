package com.myhome.controllers.dto.mapper;

import com.myhome.domain.HouseHistory;
import com.myhome.model.HouseHistoryDto;
import com.myhome.model.HouseHistoryResponse;
import org.mapstruct.Mapper;

@Mapper
public interface HouseHistoryMapper {
  HouseHistoryResponse HouseHistoryDtoToHouseHistoryResponse(HouseHistoryDto houseHistoryDto);
  HouseHistoryDto HouseHistoryToHouseHistoryDto(HouseHistory houseHistory);
  HouseHistory HouseHistoryDtoToHouseHistory(HouseHistoryDto houseHistoryDto);

}
