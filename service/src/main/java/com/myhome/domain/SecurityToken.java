package com.myhome.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityToken extends BaseEntity {
  @Column(nullable = false)
  private MyHomeTokenType tokenType;
  @Column(nullable = false, unique = true)
  private String token;
  @Column(nullable = false)
  private Date creationDate;
  @Column(nullable = false)
  private Date expiryDate;
}
