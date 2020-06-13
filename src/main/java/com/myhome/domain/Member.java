package com.myhome.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends BaseEntity {
  @Column(unique = true, nullable = false)
  private String memberId;
  @Column(nullable = false)
  private String name;
  @ManyToOne
  private House house;
}
