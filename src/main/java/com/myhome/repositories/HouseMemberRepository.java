package com.myhome.repositories;

import com.myhome.domain.HouseMember;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HouseMemberRepository extends CrudRepository<HouseMember, Long> {
  Optional<HouseMember> findByMemberId(String memberId);
}
