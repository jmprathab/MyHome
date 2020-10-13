package com.myhome.repositories;

import com.myhome.domain.HouseHistory;
import java.util.Set;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseHistoryRepository extends PagingAndSortingRepository<HouseHistory,Long> {
  Set<HouseHistory> findByHouseId(String houseId);
  Set<HouseHistory> findByMemberIdAndAndHouseId(String memberId,String houseId);

}
