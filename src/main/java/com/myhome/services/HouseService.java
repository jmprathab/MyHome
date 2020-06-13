package com.myhome.services;

import com.myhome.domain.HouseMember;

public interface HouseService {
  HouseMember addHouseMember(String houseId, HouseMember houseMember);
}
