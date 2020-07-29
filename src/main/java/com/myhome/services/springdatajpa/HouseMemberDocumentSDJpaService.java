package com.myhome.services.springdatajpa;

import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.repositories.HouseMemberDocumentRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.HouseMemberDocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class HouseMemberDocumentSDJpaService implements HouseMemberDocumentService {

    @Value("${server.files.compressionBorderSizeKBytes}")
    private int compressionBorderSizeKBytes;

    @Value("${server.files.maxSizeKBytes}")
    private int maxFileSizeKBytes;

    @Value("${server.files.compressedImageQuality}")
    private int compressedImageQuality;

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
    public boolean updateHouseMemberDocument(MultipartFile multipartFile, String memberId) throws IOException {
        return createHouseMemberDocument(multipartFile, memberId);
    }

    @Override
    public boolean createHouseMemberDocument(MultipartFile multipartFile, String memberId) throws IOException {
        boolean documentCreated = false;
        HouseMember member = houseMemberRepository.findByMemberId(memberId);
        if (member != null) {
            documentCreated = tryCreateDocument(multipartFile, member);
        }
        return documentCreated;
    }

    private boolean tryCreateDocument(MultipartFile multipartFile, HouseMember member) throws IOException {

        boolean documentCreated = false;
        File memberDocumentFile = createMemberDocumentFile(member);

        BufferedImage documentImage = getImageFromMultipartFile(multipartFile);
        if (multipartFile.getSize() < 1024 * compressionBorderSizeKBytes) {
            ImageIO.write(documentImage, "jpg", memberDocumentFile);
        } else {
            compressImageToFile(memberDocumentFile, documentImage);
        }
        if (memberDocumentFile.length() < 1024 * maxFileSizeKBytes) {
            addDocumentToHouseMember(memberDocumentFile, member);
            documentCreated = true;
        }

        memberDocumentFile.delete();
        return documentCreated;
    }

    private HouseMember addDocumentToHouseMember(File documentFile, HouseMember member) throws IOException {
        HouseMemberDocument houseMemberDocument = saveHouseMemberDocument(documentFile);
        member.setHouseMemberDocument(houseMemberDocument);
        member = houseMemberRepository.save(member);
        return member;
    }

    private HouseMemberDocument saveHouseMemberDocument(File documentFile) throws IOException {
        HouseMemberDocument newDocument = new HouseMemberDocument();
        newDocument.setDocumentFilename(documentFile.getName());
        newDocument.setDocument(Files.readAllBytes(documentFile.toPath()));
        newDocument = houseMemberDocumentRepository.save(newDocument);
        return newDocument;
    }

    private void compressImageToFile(File memberDocumentFile, BufferedImage bufferedImage) throws IOException {

        try (OutputStream memberDocumentFileOutStream = new FileOutputStream(memberDocumentFile);
             ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(memberDocumentFileOutStream)) {

            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            imageWriter.setOutput(imageOutStream);
            ImageWriteParam param = imageWriter.getDefaultWriteParam();

            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(compressedImageQuality / 100f);
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
