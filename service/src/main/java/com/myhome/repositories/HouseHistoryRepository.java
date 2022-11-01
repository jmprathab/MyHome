package com.myhome.repositories;

import com.myhome.domain.HouseRental;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseHistoryRepository extends PagingAndSortingRepository<HouseRental,Long> {

    Optional<List<HouseRental>> findAllByCommunityHouse_HouseId(String houseId, Pageable pageable);
}