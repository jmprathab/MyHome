package com.myhome.repositories;

import com.myhome.domain.HouseMemberDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface HouseMemberDocumentRepository extends JpaRepository<HouseMemberDocument, Long> {
}
