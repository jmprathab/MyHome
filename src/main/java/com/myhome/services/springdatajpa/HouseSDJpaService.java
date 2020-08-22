/*
 * Copyright 2020 Prathab Murugan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myhome.services.springdatajpa;

import com.myhome.controllers.dto.HouseHistoryDto;
import com.myhome.controllers.dto.mapper.HouseMemberMapper;
import com.myhome.domain.CommunityHouse;
import com.myhome.domain.HouseHistory;
import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.repositories.CommunityHouseRepository;
import com.myhome.repositories.HouseHistoryRepository;

import com.myhome.repositories.HouseMemberDocumentRepository;

import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseService;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
public class HouseSDJpaService implements HouseService {
    private final HouseMemberRepository houseMemberRepository;
    private final HouseMemberDocumentRepository houseMemberDocumentRepository;
    private final CommunityHouseRepository communityHouseRepository;
    private final HouseHistoryRepository houseHistoryRepository;
    private final HouseMemberMapper houseMemberMapper;


    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Set<CommunityHouse> listAllHouses() {
        Set<CommunityHouse> communityHouses = new HashSet<>();
        communityHouseRepository.findAll().forEach(communityHouses::add);
        return communityHouses;
    }

    @Override
    public Set<CommunityHouse> listAllHouses(Pageable pageable) {
        Set<CommunityHouse> communityHouses = new HashSet<>();
        communityHouseRepository.findAll(pageable).forEach(communityHouses::add);
        return communityHouses;
    }

    @Override
    public Set<HouseMember> addHouseMembers(String houseId, Set<HouseMember> houseMembers) {
        CommunityHouse communityHouse = communityHouseRepository.findByHouseId(houseId);
        Set<HouseMember> savedMembers = new HashSet<>();
        if (communityHouse != null) {
            for (HouseMember member : houseMembers) {
                HouseMemberDocument document = new HouseMemberDocument();
                member.setHouseMemberDocument(document);
                houseMemberDocumentRepository.save(document);
            }
            houseMembers.forEach(member -> member.setMemberId(generateUniqueId()));
            houseMembers.forEach(member -> member.setCommunityHouse(communityHouse));
            houseMemberRepository.saveAll(houseMembers).forEach(savedMembers::add);

            communityHouse.getHouseMembers().addAll(savedMembers);
            communityHouseRepository.save(communityHouse);
        }
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

    @Override
    public Optional<CommunityHouse> getHouseDetailsById(String houseId) {
        return Optional.ofNullable(communityHouseRepository.findByHouseId(houseId));
    }

    @Override
    public Optional<List<HouseMember>> getHouseMembersById(String houseId, Pageable pageable) {
        return Optional.ofNullable(
                houseMemberRepository.findAllByCommunityHouse_HouseId(houseId, pageable)
        );
    }

    @Override
    public HouseHistory addInterval(HouseHistoryDto houseHistoryDto) {
        HouseHistory houseHistory = houseMemberMapper.houseHistoryDtoToHouseHistory(houseHistoryDto);
        return houseHistoryRepository.save(houseHistory);
    }

    @Override
    public List<HouseHistory> getHouseHistory(String houseId, String memberId) {
        if (houseId != null && !houseId.isEmpty() && memberId != null && !memberId.isEmpty()) {
            return houseHistoryRepository.findByHouseIdAndMemberId(houseId, memberId);
        } else {
            return houseHistoryRepository.findByHouseId(houseId);
        }
    }
}
