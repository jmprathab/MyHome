package com.myhome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseMemberDocument extends BaseEntity {

    @Column
    private String documentFilename;

    @Lob
    @Column
    @Basic(fetch = FetchType.LAZY)
    private byte[] document = new byte[0];

}
