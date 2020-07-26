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
import org.springframework.util.CollectionUtils;

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

  @Override public Set<CommunityHouse> listAllHouses() {
    Set<CommunityHouse> communityHouses = new HashSet<>();
    communityHouseRepository.findAll().forEach(communityHouses::add);
    return communityHouses;
  }

  @Override public Set<HouseMember> addHouseMembers(String houseId, Set<HouseMember> houseMembers) {
    CommunityHouse communityHouse = communityHouseRepository.findByHouseId(houseId);
    houseMembers.forEach(member -> member.setMemberId(generateUniqueId()));
    houseMembers.forEach(member -> member.setCommunityHouse(communityHouse));
    Set<HouseMember> savedMembers = new HashSet<>();
    houseMemberRepository.saveAll(houseMembers).forEach(savedMembers::add);

    communityHouse.getHouseMembers().addAll(savedMembers);
    communityHouseRepository.save(communityHouse);
    return savedMembers;
  }

  @Override
  public boolean deleteMemberFromHouse(String houseId, String memberId) {
    CommunityHouse communityHouse = communityHouseRepository.findByHouseId(houseId);
    boolean isMemberRemoved = false;
    if (communityHouse != null && !CollectionUtils.isEmpty(communityHouse.getHouseMembers())) {
      Set<HouseMember> houseMembers = communityHouse.getHouseMembers();
      for (HouseMember member : houseMembers) {
        if (member.getMemberId().equals(memberId)) {
          houseMembers.remove(member);
          communityHouse.setHouseMembers(houseMembers);
          isMemberRemoved = true;
        }
      }
    }
    return isMemberRemoved;
  }

  @Override public CommunityHouse getHouseDetailsById(String houseId) {
    return communityHouseRepository.findByHouseId(houseId);
  }
}
