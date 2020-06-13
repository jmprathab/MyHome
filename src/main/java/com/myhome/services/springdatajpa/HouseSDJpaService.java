package com.myhome.services.springdatajpa;

import com.myhome.domain.HouseMember;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseService;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class HouseSDJpaService implements HouseService {
  private final HouseMemberRepository houseMemberRepository;
  private final CommunityHouseRepository communityHouseRepository;

  public HouseSDJpaService(HouseMemberRepository houseMemberRepository,
      CommunityHouseRepository communityHouseRepository) {
    this.houseMemberRepository = houseMemberRepository;
    this.communityHouseRepository = communityHouseRepository;
  }

  private String generateUniqueId() {
    return UUID.randomUUID().toString();
  }

  @Override
  public HouseMember addHouseMember(String houseId, HouseMember houseMember) {
    var communityHouse = communityHouseRepository.findByHouseId(houseId);
    houseMember.setMemberId(generateUniqueId());
    houseMember.setCommunityHouse(communityHouse);
    return houseMemberRepository.save(houseMember);
  }
}
