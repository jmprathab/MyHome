package com.myhome.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommunityAmenity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String amenityId;
  @Column(nullable = false)
  private String discription;
  @Column
  private boolean isBooked;
  @Column
  private LocalDate bookingStartDate;
  @Column
  private LocalDate bookingEndDate;
  @ManyToOne
  private Community community;

  public boolean checkBooking() {
    if(bookingEndDate == null || bookingEndDate.isBefore(LocalDate.now())) {
      isBooked = false;
    }
    return isBooked;
  }
}
