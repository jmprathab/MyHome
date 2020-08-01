package com.myhome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseMemberDocument extends BaseEntity {

    @Column(unique = true)
    private String documentFilename;

    @Lob
    @Column
    private byte[] documentContent = new byte[0];

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HouseMemberDocument)) return false;

        HouseMemberDocument that = (HouseMemberDocument) o;

        return getDocumentFilename() != null ? getDocumentFilename().equals(that.getDocumentFilename()) : that.getDocumentFilename() == null;
    }

    @Override
    public int hashCode() {
        return getDocumentFilename() != null ? getDocumentFilename().hashCode() : 0;
    }
}
