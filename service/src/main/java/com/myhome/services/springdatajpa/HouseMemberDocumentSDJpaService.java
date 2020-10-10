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

import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.repositories.HouseMemberDocumentRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseMemberDocumentService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HouseMemberDocumentSDJpaService implements HouseMemberDocumentService {

  private final HouseMemberRepository houseMemberRepository;
  private final HouseMemberDocumentRepository houseMemberDocumentRepository;
  @Value("${files.compressionBorderSizeKBytes}")
  private int compressionBorderSizeKBytes;
  @Value("${files.maxSizeKBytes}")
  private int maxFileSizeKBytes;
  @Value("${files.compressedImageQuality}")
  private float compressedImageQuality;

  public HouseMemberDocumentSDJpaService(HouseMemberRepository houseMemberRepository,
      HouseMemberDocumentRepository houseMemberDocumentRepository) {
    this.houseMemberRepository = houseMemberRepository;
    this.houseMemberDocumentRepository = houseMemberDocumentRepository;
  }

  @Override
  public Optional<HouseMemberDocument> findHouseMemberDocument(String memberId) {
    return houseMemberRepository.findByMemberId(memberId)
        .map(HouseMember::getHouseMemberDocument);
  }

  @Override
  public boolean deleteHouseMemberDocument(String memberId) {
    return houseMemberRepository.findByMemberId(memberId).map(member -> {
      if (member.getHouseMemberDocument() != null) {
        member.setHouseMemberDocument(null);
        houseMemberRepository.save(member);
        return true;
      }
      return false;
    }).orElse(false);
  }

  @Override
  public Optional<HouseMemberDocument> updateHouseMemberDocument(MultipartFile multipartFile,
      String memberId) {
    return houseMemberRepository.findByMemberId(memberId).map(member -> {
      Optional<HouseMemberDocument> houseMemberDocument = tryCreateDocument(multipartFile, member);
      houseMemberDocument.ifPresent(document -> addDocumentToHouseMember(document, member));
      return houseMemberDocument;
    }).orElse(Optional.empty());
  }

  @Override
  public Optional<HouseMemberDocument> createHouseMemberDocument(MultipartFile multipartFile,
      String memberId) {
    return houseMemberRepository.findByMemberId(memberId).map(member -> {
      Optional<HouseMemberDocument> houseMemberDocument = tryCreateDocument(multipartFile, member);
      houseMemberDocument.ifPresent(document -> addDocumentToHouseMember(document, member));
      return houseMemberDocument;
    }).orElse(Optional.empty());
  }

  private Optional<HouseMemberDocument> tryCreateDocument(MultipartFile multipartFile,
      HouseMember member) {

    try (ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream()) {
      BufferedImage documentImage = getImageFromMultipartFile(multipartFile);
      if (multipartFile.getSize() < DataSize.ofKilobytes(compressionBorderSizeKBytes).toBytes()) {
        writeImageToByteStream(documentImage, imageByteStream);
      } else {
        compressImageToByteStream(documentImage, imageByteStream);
      }
      if (imageByteStream.size() < DataSize.ofKilobytes(maxFileSizeKBytes).toBytes()) {
        HouseMemberDocument houseMemberDocument = saveHouseMemberDocument(imageByteStream,
            String.format("member_%s_document.jpg", member.getMemberId()));
        return Optional.of(houseMemberDocument);
      } else {
        return Optional.empty();
      }
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private HouseMember addDocumentToHouseMember(HouseMemberDocument houseMemberDocument,
      HouseMember member) {
    member.setHouseMemberDocument(houseMemberDocument);
    return houseMemberRepository.save(member);
  }

  private HouseMemberDocument saveHouseMemberDocument(ByteArrayOutputStream imageByteStream,
      String filename) {
    HouseMemberDocument newDocument =
        new HouseMemberDocument(filename, imageByteStream.toByteArray());
    return houseMemberDocumentRepository.save(newDocument);
  }

  private void writeImageToByteStream(BufferedImage documentImage,
      ByteArrayOutputStream imageByteStream)
      throws IOException {
    ImageIO.write(documentImage, "jpg", imageByteStream);
  }

  private void compressImageToByteStream(BufferedImage bufferedImage,
      ByteArrayOutputStream imageByteStream) throws IOException {

    try (ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(imageByteStream)) {

      ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
      imageWriter.setOutput(imageOutStream);
      ImageWriteParam param = imageWriter.getDefaultWriteParam();

      if (param.canWriteCompressed()) {
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(compressedImageQuality);
      }
      imageWriter.write(null, new IIOImage(bufferedImage, null, null), param);
      imageWriter.dispose();
    }
  }

  private BufferedImage getImageFromMultipartFile(MultipartFile multipartFile) throws IOException {
    try (InputStream multipartFileStream = multipartFile.getInputStream()) {
      return ImageIO.read(multipartFileStream);
    }
  }
}
