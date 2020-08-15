package com.myhome.repositories;

import com.myhome.domain.HouseMemberDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseMemberDocumentRepository extends JpaRepository<HouseMemberDocument, Long> {
}
