package com.myhome.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.OneToOne;

public class AmenityBookingItem extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String amenityBookingItemId;
  @OneToOne
  private Amenity amenity;
  @Column
  private LocalDateTime bookingStartDate;
  @Column
  private LocalDateTime bookingEndDate;
  @Column
  private User bookingUser;
}
