package com.myhome.services.springdatajpa;

import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseMember;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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

  @Override public List<CommunityHouse> listAllHouses(String sort) {
    List<CommunityHouse> communityHouses = new ArrayList<>();
    communityHouseRepository.findAll().forEach(communityHouses::add);

    boolean isAscendingSort = sort.contentEquals("asc");
    Comparator<CommunityHouse> comparator =
        isAscendingSort ? Comparator.comparing(CommunityHouse::getName)
            : Comparator.comparing(CommunityHouse::getName).reversed();
    return communityHouses.stream().sorted(comparator).collect(Collectors.toList());
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
  public CommunityHouse deleteMemberFromHouse(String houseId, String memberId) {
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
    if (isMemberRemoved) {
      return communityHouseRepository.save(communityHouse);
    }
    return communityHouse;
  }

  @Override public CommunityHouse getHouseDetailsById(String houseId) {
    return communityHouseRepository.findByHouseId(houseId);
  }
}
