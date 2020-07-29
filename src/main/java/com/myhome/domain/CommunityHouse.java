package com.myhome.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommunityHouse extends BaseEntity {
  @ManyToOne
  private Community community;
  @Column(nullable = false)
  private String name;
  @Column(unique = true, nullable = false)
  private String houseId;
  @OneToMany(fetch = FetchType.EAGER)
  private Set<HouseMember> houseMembers = new HashSet<>();
}
