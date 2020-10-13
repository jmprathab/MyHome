package com.myhome.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
<<<<<<< HEAD
import javax.persistence.JoinColumn;
=======
>>>>>>> Added Entity
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
<<<<<<< HEAD
import lombok.With;
=======
>>>>>>> Added Entity

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
<<<<<<< HEAD
  @With
  @OneToOne
  @JoinColumn(name = "Id")
  private HouseMember houseMember;
  @With
  @OneToOne
  @JoinColumn(name = "Id")
=======
  @ManyToOne
  private HouseMember houseMember;
  @OneToOne
>>>>>>> Added Entity
  private CommunityHouse communityHouse;


}

