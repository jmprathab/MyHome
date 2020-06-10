package com.prathab.communityservice.repositories;

import com.prathab.communityservice.domain.CommunityHouse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityHouseRepository extends CrudRepository<CommunityHouse, Long> {
  CommunityHouse findByHouseId(String houseId);
}
