package com.myhome.controllers.dto.mapper;

import com.myhome.domain.HouseHistory;
import com.myhome.model.HouseHistoryDto;
import com.myhome.model.HouseHistoryRequest;
import com.myhome.model.HouseHistoryResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface HouseHistoryMapper {
  HouseHistoryResponse HouseHistoryDtoToHouseHistoryResponse(HouseHistoryDto houseHistoryDto);
  @Mappings({
      @Mapping(target = "houseId", source = "houseId")
  })
  HouseHistoryDto HouseHistoryRequestToHouseHistoryDto(String houseId, HouseHistoryRequest houseHistoryRequest);
  HouseHistoryDto HouseHistoryToHouseHistoryDto(HouseHistory houseHistory);
  HouseHistory HouseHistoryDtoToHouseHistory(HouseHistoryDto houseHistoryDto);
}
