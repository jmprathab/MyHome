package com.myhome.services;

import com.myhome.domain.HouseMemberDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface HouseMemberDocumentService {

    boolean deleteHouseMemberDocument(String memberId);

    Optional<HouseMemberDocument> findHouseMemberDocument(String memberId);

    Optional<HouseMemberDocument> updateHouseMemberDocument(MultipartFile multipartFile, String memberId) throws IOException;

    Optional<HouseMemberDocument> createHouseMemberDocument(MultipartFile multipartFile, String memberId) throws IOException;
}
