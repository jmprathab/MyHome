package com.myhome.controllers.dto.mapper;

import com.myhome.domain.HouseRental;
import com.myhome.model.RentalDto;
import com.myhome.model.RentalRequest;
import com.myhome.model.RentalResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface HouseRentalMapper {
    RentalResponse RentalDtoToRentalResponse(RentalDto houseHistoryDto);
    @Mappings({
            @Mapping(target = "houseId", source = "houseId")
    })
    RentalDto RentalRequestToRentalDto(String houseId, RentalRequest houseHistoryRequest);
    RentalDto HouseRentalToRentalDto(HouseRental houseHistory);
    RentalResponse HouseRentalToRentalResponse(HouseRental houseHistory);
    HouseRental RentalDtoToHouseRental(RentalDto houseHistoryDto);
}