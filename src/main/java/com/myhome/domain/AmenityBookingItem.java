package com.myhome.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Data
@Entity
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
