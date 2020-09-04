package com.myhome.domain;


import lombok.AllArgsConstructor;
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
  @ManyToOne(fetch = FetchType.LAZY)
  private Community community;

}
