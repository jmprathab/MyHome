package com.myhome.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseHistory extends BaseEntity{
  @Column
  private String memberId;
  @Column
  private String houseId;
  @Column
  private Date stayFromDate;
  @Column
  private Date stayToDate;
  @With
  @OneToOne
  @JoinColumn(name = "Id")
  private HouseMember houseMember;
  @With
  @OneToOne
  @JoinColumn(name = "Id")
  private CommunityHouse communityHouse;


}

