package com.prathab.communityservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommunityHouse extends CommunityBaseEntity {
  @ManyToOne
  private Community community;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String houseId;
}
