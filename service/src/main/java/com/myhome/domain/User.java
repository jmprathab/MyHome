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
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Entity identifying a valid user in the service.
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false, of = {"userId", "email"})
@Entity
@With
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "User.communities",
        attributeNodes = {
            @NamedAttributeNode("communities"),
        }
    )
})
public class User extends BaseEntity {
  @Column(nullable = false)
  private String name;
  @Column(unique = true, nullable = false)
  private String userId;
  @Column(unique = true, nullable = false)
  private String email;
  @Column(nullable = false)
  private String encryptedPassword;
  @ManyToMany(mappedBy = "admins", fetch = FetchType.LAZY)
  private Set<Community> communities = new HashSet<>();
}
