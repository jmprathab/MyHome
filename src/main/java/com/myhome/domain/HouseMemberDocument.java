package com.myhome.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"documentFilename"}, callSuper = false)
public class HouseMemberDocument extends BaseEntity {

  @Column(unique = true)
  private String documentFilename;

  @Lob
  @Column
  private byte[] documentContent = new byte[0];
}
