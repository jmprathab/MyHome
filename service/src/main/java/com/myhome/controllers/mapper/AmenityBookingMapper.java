package com.myhome.controllers.mapper;

import com.myhome.controllers.dto.AmenityBookingDto;
import com.myhome.domain.AmenityBookingItem;
import com.myhome.model.BookAmenityRequest;
import com.myhome.model.BookAmenityResponse;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {AmenityApiMapper.class, UserApiMapper.class})
public interface AmenityBookingMapper {

  @Named("toLocalDateTime")
  default LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
    return offsetDateTime.toLocalDateTime();
  }

  @Named("toOffsetDateTime")
  default OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
    return localDateTime.atOffset(ZoneOffset.UTC);
  }

  @Mapping(target = "amenity.amenityId", source = "request.amenityId")
  @Mapping(target = "user.userId", source = "request.userId")
  @Mapping(target = "startDate", qualifiedByName = "toLocalDateTime")
  @Mapping(target = "endDate", qualifiedByName = "toLocalDateTime")
  AmenityBookingDto bookAmenityRequestToAmenityBookingDto(BookAmenityRequest request);

  @Mapping(target = "amenityId", source = "dto.amenity.amenityId")
  @Mapping(target = "userId", source = "dto.user.userId")
  @Mapping(target = "startDate", qualifiedByName = "toOffsetDateTime")
  @Mapping(target = "endDate", qualifiedByName = "toOffsetDateTime")
  BookAmenityResponse amenityBookingDtoToBookAmenityResponse(AmenityBookingDto dto);

  @Mapping(target = "bookingStartDate", source = "startDate")
  @Mapping(target = "bookingEndDate", source = "endDate")
  @Mapping(target = "amenity", source = "dto.amenity", qualifiedByName = "amenityDtoToAmenity")
  @Mapping(target = "bookingUser", source = "dto.user", qualifiedByName = "userDtoToUser")
  AmenityBookingItem amenityBookingDtoToAmenityBookingItem(AmenityBookingDto dto);

  @Mapping(target = "startDate", source = "bookingStartDate")
  @Mapping(target = "endDate", source = "bookingEndDate")
  @Mapping(target = "amenity", source = "entity.amenity", qualifiedByName = "amenityToAmenityDto")
  @Mapping(target = "user", source = "entity.bookingUser", qualifiedByName = "userToUserDto")
  AmenityBookingDto amenityBookingItemToAmenityBookingDto(AmenityBookingItem entity);

}
