package com.myhome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.OffsetDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseRental extends BaseEntity {

    @Column(unique = false, nullable = false)
    private String houseId;

    @Column(nullable = false, unique = false)
    private String memberId;

    @Column(nullable = true)
    private OffsetDateTime bookingFromDate;

    @Column(nullable = true)
    private OffsetDateTime bookingToDate;

    @Column(nullable = true)
    private OffsetDateTime arrivalDate;

    @Column(nullable = true)
    private OffsetDateTime departureDate;

    @OneToOne(fetch = FetchType.EAGER)
    private CommunityHouse communityHouse;

    @OneToOne(fetch = FetchType.EAGER)
    private HouseMember houseMember;
}