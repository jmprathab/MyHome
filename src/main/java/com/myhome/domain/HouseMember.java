package com.myhome.domain;

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
public class HouseMember extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String memberId;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  private CommunityHouse communityHouse;
}
