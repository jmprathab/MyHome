package com.myhome.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommunityAmenity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String amenityId;
  @Column(nullable = false)
  private String description;
  @Column
  private boolean isBooked;
  @Column
  private LocalDateTime bookingStartDate;
  @Column
  private LocalDateTime bookingEndDate;
  @ManyToOne
  private Community community;

}
