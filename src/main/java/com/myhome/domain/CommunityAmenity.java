package com.myhome.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@With
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "CommunityAmenity.community",
        attributeNodes = {
            @NamedAttributeNode("community"),
        }
    )
})
@EqualsAndHashCode(of = {"amenityId"}, callSuper = true)
public class CommunityAmenity extends BaseEntity {
  public static final String BOOKING_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  @Column(nullable = false, unique = true)
  private String amenityId;
  @Column(nullable = false)
  private String description;
  @Column
  private boolean isBooked;
  @Column
  @JsonFormat(pattern = BOOKING_DATE_TIME_FORMAT)
  private LocalDateTime bookingStartDate;
  @Column
  @JsonFormat(pattern = BOOKING_DATE_TIME_FORMAT)
  private LocalDateTime bookingEndDate;
  @ManyToOne(fetch = FetchType.LAZY)
  private Community community;

}
