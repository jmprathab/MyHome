package com.myhome.repositories;

import com.myhome.domain.HouseHistory;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> Added Entity
import java.util.Set;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseHistoryRepository extends PagingAndSortingRepository<HouseHistory,Long> {
<<<<<<< HEAD
  List<HouseHistory> findByHouseId(String houseId);
  List<HouseHistory> findByMemberIdAndAndHouseId(String memberId,String houseId);
=======
  Set<HouseHistory> findByHouseId(String houseId);
  Set<HouseHistory> findByMemberIdAndAndHouseId(String memberId,String houseId);
>>>>>>> Added Entity

}
