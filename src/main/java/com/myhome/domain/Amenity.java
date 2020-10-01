package com.myhome.domain;

import java.math.BigDecimal;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@With
@NamedEntityGraph(
    name = "Amenity.community",
    attributeNodes = {
        @NamedAttributeNode("community"),
    }
)
public class Amenity extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String amenityId;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String description;
  @Column(nullable = false)
  private BigDecimal price;
  @ManyToOne(fetch = FetchType.LAZY)
  private Community community;
  @ManyToOne
  private CommunityHouse communityHouse;
}
