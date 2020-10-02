package com.myhome.services;

import com.myhome.domain.HouseMemberDocument;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface HouseMemberDocumentService {

  boolean deleteHouseMemberDocument(String memberId);

  Optional<HouseMemberDocument> findHouseMemberDocument(String memberId);

  Optional<HouseMemberDocument> updateHouseMemberDocument(MultipartFile multipartFile,
      String memberId);

  Optional<HouseMemberDocument> createHouseMemberDocument(MultipartFile multipartFile,
      String memberId);
}
