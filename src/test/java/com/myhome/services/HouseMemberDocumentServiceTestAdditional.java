package com.myhome.services;

import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.repositories.HouseMemberDocumentRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.services.springdatajpa.HouseMemberDocumentSDJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class HouseMemberDocumentServiceTestAdditional {

    private static final String MEMBER_ID = "test-member-id";
    private static final String MEMBER_NAME = "test-member-name";
    private static final HouseMemberDocument MEMBER_DOCUMENT = new HouseMemberDocument("test-file-name", new byte[0]);

    @Value("${files.compressionBorderSizeKBytes}")
    private int compressionBorderSizeKBytes;

    @MockBean
    private HouseMemberRepository houseMemberRepository;

    @MockBean
    private HouseMemberDocumentRepository houseMemberDocumentRepository;
    
    @Autowired
    private HouseMemberDocumentSDJpaService houseMemberDocumentService;

    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);
    }

    private byte[] getImageAsByteArray(int height, int width) throws IOException {
        BufferedImage documentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        try(ByteArrayOutputStream imageBytesStream = new ByteArrayOutputStream()) {
            ImageIO.write(documentImage, "jpg", imageBytesStream);
            return imageBytesStream.toByteArray();
        }
    }

    @Test
    void updateHouseMemberDocumentSuccess() throws IOException {
        // given
        byte[] imageBytes = getImageAsByteArray(10, 10);
        MockMultipartFile newDocumentFile = new MockMultipartFile("new-test-file-name", imageBytes);
        HouseMemberDocument savedDocument = new HouseMemberDocument(String.format("member_%s_document.jpg", MEMBER_ID), imageBytes);
        HouseMember testMember = new HouseMember(MEMBER_ID, MEMBER_DOCUMENT, MEMBER_NAME, null);

        given(houseMemberRepository.findByMemberId(MEMBER_ID))
                .willReturn(Optional.of(testMember));
        given(houseMemberDocumentRepository.save(savedDocument))
                .willReturn(savedDocument);
        // when
        Optional<HouseMemberDocument> houseMemberDocument = houseMemberDocumentService.updateHouseMemberDocument(newDocumentFile, MEMBER_ID);

        // then
        assertTrue(houseMemberDocument.isPresent());
        assertEquals(testMember.getHouseMemberDocument(), houseMemberDocument.get());
        verify(houseMemberRepository).findByMemberId(MEMBER_ID);
        verify(houseMemberDocumentRepository).save(savedDocument);
        verify(houseMemberRepository).save(testMember);
    }

    @Test
    void updateHouseMemberDocumentMemberNotExists() throws IOException {
        // given
        byte[] imageBytes = getImageAsByteArray(10, 10);
        MockMultipartFile newDocumentFile = new MockMultipartFile("new-test-file-name", imageBytes);

        given(houseMemberRepository.findByMemberId(MEMBER_ID))
                .willReturn(Optional.empty());
        // when
        Optional<HouseMemberDocument> houseMemberDocument = houseMemberDocumentService.updateHouseMemberDocument(newDocumentFile, MEMBER_ID);

        // then
        assertFalse(houseMemberDocument.isPresent());
        verify(houseMemberRepository).findByMemberId(MEMBER_ID);
        verify(houseMemberDocumentRepository, never()).save(any());
        verify(houseMemberRepository, never()).save(any());

    }

    @Test
    void updateHouseMemberDocumentTooLargeFile() throws IOException {
        // given
        byte[] imageBytes = getImageAsByteArray(10, 10);
        MockMultipartFile tooLargeDocumentFile = new MockMultipartFile("new-test-file-name", imageBytes);
        HouseMember testMember = new HouseMember(MEMBER_ID, MEMBER_DOCUMENT, MEMBER_NAME, null);

        given(houseMemberRepository.findByMemberId(MEMBER_ID))
                .willReturn(Optional.of(testMember));
        // when
        Optional<HouseMemberDocument> houseMemberDocument = houseMemberDocumentService.updateHouseMemberDocument(tooLargeDocumentFile, MEMBER_ID);

        // then
        assertFalse(houseMemberDocument.isPresent());
        assertEquals(testMember.getHouseMemberDocument(), MEMBER_DOCUMENT);
        verify(houseMemberRepository).findByMemberId(MEMBER_ID);
        verify(houseMemberDocumentRepository, never()).save(any());
        verify(houseMemberRepository, never()).save(any());
    }

    @Test
    void createHouseMemberDocumentSuccess() throws IOException {
        // given
        byte[] imageBytes = getImageAsByteArray(10, 10);
        HouseMemberDocument savedDocument = new HouseMemberDocument(String.format("member_%s_document.jpg", MEMBER_ID), imageBytes);
        MockMultipartFile newDocumentFile = new MockMultipartFile("new-test-file-name", imageBytes);
        HouseMember testMember = new HouseMember(MEMBER_ID, MEMBER_DOCUMENT, MEMBER_NAME, null);

        given(houseMemberRepository.findByMemberId(MEMBER_ID))
                .willReturn(Optional.of(testMember));
        given(houseMemberDocumentRepository.save(savedDocument))
                .willReturn(savedDocument);
        // when
        Optional<HouseMemberDocument> houseMemberDocument = houseMemberDocumentService.createHouseMemberDocument(newDocumentFile, MEMBER_ID);

        // then
        assertTrue(houseMemberDocument.isPresent());
        assertNotEquals(testMember.getHouseMemberDocument().getDocumentFilename(), MEMBER_DOCUMENT.getDocumentFilename());
        verify(houseMemberRepository).findByMemberId(MEMBER_ID);
        verify(houseMemberDocumentRepository).save(savedDocument);
        verify(houseMemberRepository).save(testMember);
    }

    @Test
    void createHouseMemberDocumentMemberNotExists() throws IOException {
        // given
        byte[] imageBytes = getImageAsByteArray(10, 10);
        MockMultipartFile newDocumentFile = new MockMultipartFile("new-test-file-name", imageBytes);

        given(houseMemberRepository.findByMemberId(MEMBER_ID))
                .willReturn(Optional.empty());
        // when
        Optional<HouseMemberDocument> houseMemberDocument = houseMemberDocumentService.createHouseMemberDocument(newDocumentFile, MEMBER_ID);

        // then
        assertFalse(houseMemberDocument.isPresent());
        verify(houseMemberRepository).findByMemberId(MEMBER_ID);
        verify(houseMemberDocumentRepository, never()).save(any());
        verify(houseMemberRepository, never()).save(any());
    }

    @Test
    void createHouseMemberDocumentTooLargeFile() throws IOException {
        // given
        byte[] imageBytes = getImageAsByteArray(1, 1);
        MockMultipartFile tooLargeDocumentFile = new MockMultipartFile("new-test-file-name", imageBytes);
        HouseMember testMember = new HouseMember(MEMBER_ID, MEMBER_DOCUMENT, MEMBER_NAME, null);

        given(houseMemberRepository.findByMemberId(MEMBER_ID))
                .willReturn(Optional.of(testMember));
        // when
        Optional<HouseMemberDocument> houseMemberDocument = houseMemberDocumentService.createHouseMemberDocument(tooLargeDocumentFile, MEMBER_ID);

        // then
        assertFalse(houseMemberDocument.isPresent());
        assertEquals(testMember.getHouseMemberDocument(), MEMBER_DOCUMENT);
        verify(houseMemberRepository).findByMemberId(MEMBER_ID);
        verify(houseMemberDocumentRepository, never()).save(any());
        verify(houseMemberRepository, never()).save(any());
    }
    
}
