package com.myhome.services.springdatajpa;

import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.repositories.HouseMemberDocumentRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseMemberDocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class HouseMemberDocumentSDJpaService implements HouseMemberDocumentService {

    @Value("${files.compressionBorderSizeKBytes}")
    private int compressionBorderSizeKBytes;

    @Value("${files.maxSizeKBytes}")
    private int maxFileSizeKBytes;

    @Value("${files.compressedImageQuality}")
    private float compressedImageQuality;

    private final HouseMemberRepository houseMemberRepository;
    private final HouseMemberDocumentRepository houseMemberDocumentRepository;

    public HouseMemberDocumentSDJpaService(HouseMemberRepository houseMemberRepository, HouseMemberDocumentRepository houseMemberDocumentRepository) {
        this.houseMemberRepository = houseMemberRepository;
        this.houseMemberDocumentRepository = houseMemberDocumentRepository;
    }

    @Override
    public Optional<HouseMemberDocument> findHouseMemberDocument(String memberId) {
        return houseMemberRepository.findByMemberId(memberId)
                .map(member -> member.getHouseMemberDocument());
    }

    @Override
    public boolean deleteHouseMemberDocument(String memberId) {
        return houseMemberRepository.findByMemberId(memberId).map(member -> {
            if(member.getHouseMemberDocument() != null) {
                member.setHouseMemberDocument(null);
                houseMemberRepository.save(member);
            }
            return true;
        }).orElse(false);
    }

    @Override
    public Optional<HouseMemberDocument> updateHouseMemberDocument(MultipartFile multipartFile, String memberId) throws IOException {
        return houseMemberRepository.findByMemberId(memberId).map(member -> {
            Optional<HouseMemberDocument> houseMemberDocument = tryCreateDocument(multipartFile, member);
            houseMemberDocument.ifPresent(document -> addDocumentToHouseMember(document, member));
            return houseMemberDocument;
        }).orElse(Optional.empty());
    }

    @Override
    public Optional<HouseMemberDocument> createHouseMemberDocument(MultipartFile multipartFile, String memberId) throws IOException {
        return houseMemberRepository.findByMemberId(memberId).map(member -> {
            Optional<HouseMemberDocument> houseMemberDocument = tryCreateDocument(multipartFile, member);
            houseMemberDocument.ifPresent(document -> addDocumentToHouseMember(document, member));
            return houseMemberDocument;
        }).orElse(Optional.empty());
    }

    private Optional<HouseMemberDocument> tryCreateDocument(MultipartFile multipartFile, HouseMember member) {

        try(ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream()) {
            BufferedImage documentImage = getImageFromMultipartFile(multipartFile);
            if (multipartFile.getSize() < DataSize.ofKilobytes(compressionBorderSizeKBytes).toBytes()) {
                writeImageToByteStream(documentImage, imageByteStream);
            } else {
                compressImageToByteStream(documentImage, imageByteStream);
            }
            if (imageByteStream.size() < DataSize.ofKilobytes(maxFileSizeKBytes).toBytes()) {
                HouseMemberDocument houseMemberDocument = saveHouseMemberDocument(imageByteStream, String.format("member_%s_document.jpg", member.getMemberId()));
                return Optional.of(houseMemberDocument);
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private HouseMember addDocumentToHouseMember(HouseMemberDocument houseMemberDocument, HouseMember member) {
        member.setHouseMemberDocument(houseMemberDocument);
        member = houseMemberRepository.save(member);
        return member;
    }

    private HouseMemberDocument saveHouseMemberDocument(ByteArrayOutputStream imageByteStream, String filename) throws IOException {
        HouseMemberDocument newDocument = new HouseMemberDocument(filename, imageByteStream.toByteArray());
        newDocument = houseMemberDocumentRepository.save(newDocument);
        return newDocument;
    }

    private void writeImageToByteStream(BufferedImage documentImage, ByteArrayOutputStream imageByteStream) throws IOException {
        ImageIO.write(documentImage, "jpg", imageByteStream);
    }

    private void compressImageToByteStream(BufferedImage bufferedImage, ByteArrayOutputStream imageByteStream) throws IOException {

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
