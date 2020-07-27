package com.myhome.services;

import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import java.util.Set;

public interface HouseService {
  Set<CommunityHouse> listAllHouses();

  Set<HouseMember> addHouseMembers(String houseId, Set<HouseMember> houseMembers);

  boolean deleteMemberFromHouse(String houseId, String memberId);

  CommunityHouse getHouseDetailsById(String houseId);
}
