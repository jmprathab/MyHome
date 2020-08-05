package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.HouseHistoryDto;
import com.myhome.controllers.dto.mapper.HouseMemberMapper;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseHistory;
import com.myhome.domain.HouseMember;
import com.myhome.helper.CommonHelper;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.HouseHistoryRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseService;

import java.util.*;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
public class HouseSDJpaService implements HouseService {
  private final HouseMemberRepository houseMemberRepository;
  private final CommunityHouseRepository communityHouseRepository;
  private final HouseHistoryRepository houseHistoryRepository;
  private final HouseMemberMapper houseMemberMapper;

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
          communityHouseRepository.save(communityHouse);
          member.setCommunityHouse(null);
          houseMemberRepository.save(member);
          isMemberRemoved = true;
          break;
        }
      }
    }
    return isMemberRemoved;
  }

  @Override public Optional<CommunityHouse> getHouseDetailsById(String houseId) {
    CommunityHouse house = communityHouseRepository.findByHouseId(houseId);
    return house == null ? Optional.empty() : Optional.of(house);
  }

  @Override
  public HouseHistory addInterval(HouseHistoryDto houseHistoryDto) {

    HouseHistory houseHistoryPO = houseMemberMapper.houseHistoryDtoToHouseHistory(houseHistoryDto);
    houseHistoryRepository.save(houseHistoryPO);
    return houseHistoryPO;
  }

  @Override
  public List<HouseHistory> getHouseHistory(String houseId, String memberId) {
    if(!CommonHelper.empty(houseId) && !CommonHelper.empty(memberId)){
      return  houseHistoryRepository.findByHouseIdAndMemberId(houseId,memberId);
    }
    else{
      return  houseHistoryRepository.findByHouseId(houseId);
    }
  }
}
