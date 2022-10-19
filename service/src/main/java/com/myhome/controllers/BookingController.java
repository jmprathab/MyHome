package com.myhome.controllers;

import com.myhome.api.BookingsApi;
import com.myhome.controllers.mapper.BookingApiMapper;
import com.myhome.utils.StringTimeFormatConverter;
import com.myhome.domain.AmenityBookingItem;
import com.myhome.model.GetBookingDetailsResponse;
import com.myhome.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BookingController implements BookingsApi {

  private final BookingService bookingSDJpaService;
  private final BookingApiMapper bookingApiMapper;


  @Override
  public ResponseEntity<Void> deleteBooking(@PathVariable String amenityId,
                                            @PathVariable String bookingId) {
    boolean isBookingDeleted = bookingSDJpaService.deleteBooking(amenityId, bookingId);
    if (isBookingDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }


  @Override
  public ResponseEntity<Set<GetBookingDetailsResponse>> getBookingsForAmenityWithOptionalTimeRange(@PathVariable(name = "amenityId") String amenityId,
                                                                      @RequestParam(defaultValue = " ") String start,
                                                                      @RequestParam(defaultValue = " ") String end,
                                                                      @PageableDefault Pageable pageable) {
    Set<GetBookingDetailsResponse> result = new HashSet<>();
    Set<AmenityBookingItem> items;

    if(start.isBlank() || end.isBlank()){
      items = bookingSDJpaService.getAllBookingForAmenity(amenityId, pageable);
    }else {
      LocalDateTime startDate = StringTimeFormatConverter.stringToLocalDateTime(start);
      LocalDateTime endDate = StringTimeFormatConverter.stringToLocalDateTime(end);
      items = bookingSDJpaService.getAllBookingForAmenityBetween(amenityId, startDate, endDate, pageable);
    }
    for(AmenityBookingItem item: items){
      result.add(bookingApiMapper.bookingToBookingDetailsResponse(item));
    }
    return items.isEmpty()? ResponseEntity.status(HttpStatus.NO_CONTENT).build() : ResponseEntity.ok(result);
  }
}