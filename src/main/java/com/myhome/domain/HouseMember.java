package com.myhome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseMember extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String memberId;

  @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "document_id")
  private HouseMemberDocument houseMemberDocument;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  private CommunityHouse communityHouse;
}
