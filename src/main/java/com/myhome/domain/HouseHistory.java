package com.myhome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

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
}
