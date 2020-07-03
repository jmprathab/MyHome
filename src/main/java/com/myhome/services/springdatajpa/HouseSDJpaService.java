package com.myhome.services.springdatajpa;

import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseService;
import java.util.HashSet;
import java.util.Set;
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

  @Override public Set<HouseMember> addHouseMembers(String houseId, Set<HouseMember> houseMembers) {
    CommunityHouse communityHouse = communityHouseRepository.findByHouseId(houseId);
    houseMembers.forEach(member -> member.setMemberId(generateUniqueId()));
    houseMembers.forEach(member -> member.setCommunityHouse(communityHouse));
    Set<HouseMember> savedMembers = new HashSet<HouseMember>();
    houseMemberRepository.saveAll(houseMembers).forEach(savedMembers::add);
    return savedMembers;
  }
}
