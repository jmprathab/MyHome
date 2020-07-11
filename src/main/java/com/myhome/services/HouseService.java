package com.myhome.services;

import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import java.util.Set;

public interface HouseService {
  Set<HouseMember> addHouseMembers(String houseId, Set<HouseMember> houseMembers);

  CommunityHouse deleteMemberFromHouse(String houseId, String memberId);
}
