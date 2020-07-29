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
import java.io.*;
import java.nio.file.Files;
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
        HouseMember member = houseMemberRepository.findByMemberId(memberId);
        if(member != null) {
            return Optional.ofNullable(member.getHouseMemberDocument());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteHouseMemberDocument(String memberId) {
        boolean documentDeleted = false;
        HouseMember member = houseMemberRepository.findByMemberId(memberId);
        if (member != null) {
            houseMemberDocumentRepository.delete(member.getHouseMemberDocument());
            member.setHouseMemberDocument(null);
            houseMemberRepository.save(member);
            documentDeleted = true;
        }
        return documentDeleted;
    }

    @Override
    public Optional<HouseMemberDocument> updateHouseMemberDocument(MultipartFile multipartFile, String memberId) throws IOException {
        HouseMember member = houseMemberRepository.findByMemberId(memberId);
        if (member != null) {
            Optional<HouseMemberDocument> houseMemberDocument = tryCreateDocument(multipartFile, member);
            houseMemberDocument.ifPresent(document -> {
                member.setHouseMemberDocument(document);
                houseMemberRepository.save(member);
            });
            return houseMemberDocument;
        } else {
            return Optional.empty();
        }    }

    @Override
    public Optional<HouseMemberDocument> createHouseMemberDocument(MultipartFile multipartFile, String memberId) throws IOException {
        HouseMember member = houseMemberRepository.findByMemberId(memberId);
        if (member != null) {
            Optional<HouseMemberDocument> houseMemberDocument = tryCreateDocument(multipartFile, member);
            houseMemberDocument.map(document -> addDocumentToHouseMember(document, member));
            return houseMemberDocument;
        } else {
            return Optional.empty();
        }
    }

    private Optional<HouseMemberDocument> tryCreateDocument(MultipartFile multipartFile, HouseMember member) throws IOException {

        File memberDocumentFile = createMemberDocumentFile(member);

        try {
            BufferedImage documentImage = getImageFromMultipartFile(multipartFile);
            if (multipartFile.getSize() < DataSize.ofKilobytes(compressionBorderSizeKBytes).toBytes()) {
                writeImageToFile(documentImage, memberDocumentFile);
            } else {
                compressImageToFile(memberDocumentFile, documentImage);
            }
            if (memberDocumentFile.length() < DataSize.ofKilobytes(maxFileSizeKBytes).toBytes()) {
                HouseMemberDocument houseMemberDocument = saveHouseMemberDocument(memberDocumentFile);
                return Optional.of(houseMemberDocument);
            } else {
                return Optional.empty();
            }
        } finally {
            memberDocumentFile.delete();
        }
    }

    private HouseMember addDocumentToHouseMember(HouseMemberDocument houseMemberDocument, HouseMember member) {
        member.setHouseMemberDocument(houseMemberDocument);
        return houseMemberRepository.save(member);
    }

    private HouseMemberDocument saveHouseMemberDocument(File documentFile) throws IOException {
        HouseMemberDocument newDocument = new HouseMemberDocument(documentFile.getName(), Files.readAllBytes(documentFile.toPath()));
        return houseMemberDocumentRepository.save(newDocument);
    }

    private void writeImageToFile(BufferedImage documentImage, File memberDocumentFile) throws IOException {
        ImageIO.write(documentImage, "jpg", memberDocumentFile);
    }

    private void compressImageToFile(File memberDocumentFile, BufferedImage bufferedImage) throws IOException {

        try (OutputStream memberDocumentFileOutStream = new FileOutputStream(memberDocumentFile);
             ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(memberDocumentFileOutStream)) {

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

    private File createMemberDocumentFile(HouseMember member) {
        File memberDocumentFile = new File("member_" + member.getMemberId() + "_document.jpg");
        return memberDocumentFile;
    }

}