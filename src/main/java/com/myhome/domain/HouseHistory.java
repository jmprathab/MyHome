package com.myhome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseHistory  extends BaseEntity {

    @Column(unique = false, nullable = false)
    private String houseId;

    @Column(nullable = false, unique = false)
    private String memberId;

    @Column(nullable = true)
    private Date fromDate;

    @Column(nullable = true)
    private Date toDate;

    @OneToOne(fetch = FetchType.EAGER)
    private CommunityHouse communityHouse;

    @OneToOne(fetch = FetchType.EAGER)
    private HouseMember houseMember;
}
