package com.myhome.repositories;

import com.myhome.domain.HouseHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseHistoryRepository extends CrudRepository<HouseHistory, Long> {
    List<HouseHistory> findByHouseId(String houseId);
    List<HouseHistory> findByHouseIdAndMemberId(String houseId,String memberId);
}
