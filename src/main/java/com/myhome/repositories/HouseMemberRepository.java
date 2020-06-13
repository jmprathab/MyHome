package com.myhome.repositories;

import com.myhome.domain.HouseMember;
import org.springframework.data.repository.CrudRepository;

public interface HouseMemberRepository extends CrudRepository<HouseMember, Long> {
  HouseMember findByMemberId(String memberId);
}
