package com.myhome.repositories;

import com.myhome.domain.CommunityHouse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityHouseRepository extends CrudRepository<CommunityHouse, Long> {
  CommunityHouse findByHouseId(String houseId);
  void deleteByHouseId(String houseId);
}
