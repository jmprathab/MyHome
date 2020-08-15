/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
//@EqualsAndHashCode(exclude = "community", callSuper = false)
public class CommunityHouse extends BaseEntity {
  @With
  @ManyToOne(fetch = FetchType.EAGER)
  private Community community;
  @Column(nullable = false)
  private String name;
  @With
  @Column(unique = true, nullable = false)
  private String houseId;
  @OneToMany(fetch = FetchType.EAGER)
  private Set<HouseMember> houseMembers = new HashSet<>();
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (this.hashCode() == obj.hashCode()) {
      return true;
    }

    if (!(obj instanceof CommunityHouse)) {
      return false;
    } else {
      CommunityHouse house = (CommunityHouse)obj;

      return house.getHouseMembers().size() == houseMembers.size() && house.community.equals(community) && house.name.equals(name) && house.houseId.equals(houseId) && getId().equals(house.getId());
    }
  }
}
