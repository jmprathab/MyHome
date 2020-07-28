package com.myhome.repositories;

import com.myhome.domain.HouseMemberDocument;
import org.springframework.data.repository.CrudRepository;

public interface HouseMemberDocumentRepository extends CrudRepository<HouseMemberDocument, Long> {
}
