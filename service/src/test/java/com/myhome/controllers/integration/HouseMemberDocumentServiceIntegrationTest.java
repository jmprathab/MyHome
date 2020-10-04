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

package com.myhome.controllers.integration;

import com.myhome.controllers.request.CreateUserRequest;
import com.myhome.controllers.request.LoginUserRequest;
import com.myhome.domain.HouseMember;
import com.myhome.domain.HouseMemberDocument;
import com.myhome.repositories.HouseMemberDocumentRepository;
import com.myhome.repositories.HouseMemberRepository;
import com.myhome.repositories.UserRepository;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "files.maxSizeKBytes=1",
    "files.compressionBorderSizeKBytes=99",
    "files.compressedImageQuality=0.99",
    "spring.datasource.initialization-mode=never"
})
public class HouseMemberDocumentServiceIntegrationTest extends ControllerIntegrationTestBase {

  private static final String MEMBER_ID = "default-member-id-for-testing";
  private static final String TEST_DOCUMENT_NAME = "test-document";
  private static final String TESTING_ENDPOINT_URL = "/members/{memberId}/documents";
  private static final String TEST_USERNAME = "Test User";
  private static final String TEST_USER_EMAIL = "testuser@myhome.com";
  private static final String TEST_USER_PASSWORD = "testpassword";
  @Autowired
  private HouseMemberDocumentRepository houseMemberDocumentRepository;
  @Autowired
  private HouseMemberRepository houseMemberRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    if (!houseMemberRepository.findByMemberId(MEMBER_ID).isPresent()) {
      HouseMember member =
          houseMemberRepository.save(new HouseMember(MEMBER_ID, null, "test-member-name", null));
      member.setMemberId(MEMBER_ID);
      houseMemberRepository.save(member);
    }
    authDefaultUser();
  }

  @AfterEach()
  void cleanHouseMemberDocument() {
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    member.setHouseMemberDocument(null);
    houseMemberRepository.save(member);
  }

  @Test
  void getHouseMemberDocumentSuccess() throws Exception {
    // given
    addDefaultHouseMemberDocument();

    // when
    MvcResult mvcResult = mockMvc.perform(get(TESTING_ENDPOINT_URL, MEMBER_ID)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();

    // then
    MockHttpServletResponse response = mvcResult.getResponse();
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    assertEquals(response.getContentType(), MediaType.IMAGE_JPEG_VALUE);
    assertEquals(response.getContentAsByteArray().length,
        member.getHouseMemberDocument().getDocumentContent().length);
  }

  @Test
  void getHouseMemberDocumentMemberNotExists() throws Exception {
    // given
    addDefaultHouseMemberDocument();

    // when
    mockMvc.perform(get(TESTING_ENDPOINT_URL, "non-existing-member-id")
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void addHouseMemberDocumentSuccess() throws Exception {
    // given
    byte[] imageBytes = getImageAsByteArray(10, 10);
    MockMultipartFile mockImageFile = new MockMultipartFile("memberDocument", imageBytes);

    // when
    mockMvc.perform(MockMvcRequestBuilders.multipart(TESTING_ENDPOINT_URL, MEMBER_ID)
        .file(mockImageFile)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNoContent());

    // then
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    assertEquals(member.getHouseMemberDocument().getDocumentFilename(),
        String.format("member_%s_document.jpg", MEMBER_ID));
  }

  @Test
  void addHouseMemberDocumentMemberNotExists() throws Exception {
    // given
    byte[] imageBytes = getImageAsByteArray(1000, 1000);
    MockMultipartFile mockImageFile = new MockMultipartFile("memberDocument", imageBytes);

    // when
    mockMvc.perform(MockMvcRequestBuilders.multipart(TESTING_ENDPOINT_URL, "non-exist-member-id")
        .file(mockImageFile)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNotFound());

    // then
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    assertNull(member.getHouseMemberDocument());
  }

  @Test
  void addHouseMemberDocumentTooLargeFile() throws Exception {
    // given
    byte[] imageBytes = getImageAsByteArray(1000, 1000);
    MockMultipartFile mockImageFile = new MockMultipartFile("memberDocument", imageBytes);

    // when
    mockMvc.perform(MockMvcRequestBuilders.multipart(TESTING_ENDPOINT_URL, MEMBER_ID)
        .file(mockImageFile)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNotFound());

    // then
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    assertNull(member.getHouseMemberDocument());
  }

  @Test
  void putHouseMemberDocumentSuccess() throws Exception {
    // given
    byte[] imageBytes = getImageAsByteArray(10, 10);
    MockMultipartFile mockImageFile = new MockMultipartFile("memberDocument", imageBytes);
    addDefaultHouseMemberDocument();

    // when
    mockMvc.perform(MockMvcRequestBuilders.multipart(TESTING_ENDPOINT_URL, MEMBER_ID)
        .file(mockImageFile)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNoContent());

    // then
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    assertEquals(member.getHouseMemberDocument().getDocumentFilename(),
        String.format("member_%s_document.jpg", member.getMemberId()));
  }

  @Test
  void putHouseMemberDocumentMemberNotExists() throws Exception {
    // given
    byte[] imageBytes = getImageAsByteArray(10, 10);
    MockMultipartFile mockImageFile = new MockMultipartFile("memberDocument", imageBytes);
    addDefaultHouseMemberDocument();

    // when
    mockMvc.perform(MockMvcRequestBuilders.multipart(TESTING_ENDPOINT_URL, "non-exist-member-id")
        .file(mockImageFile)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void putHouseMemberDocumentTooLargeFile() throws Exception {
    // given
    byte[] imageBytes = getImageAsByteArray(1000, 1000);
    MockMultipartFile mockImageFile = new MockMultipartFile("memberDocument", imageBytes);
    addDefaultHouseMemberDocument();

    // when
    mockMvc.perform(MockMvcRequestBuilders.multipart(TESTING_ENDPOINT_URL, MEMBER_ID)
        .file(mockImageFile)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNotFound());

    // then
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    assertNotEquals(member.getHouseMemberDocument().getDocumentFilename(),
        String.format("member_%s_document.jpg", member.getMemberId()));
  }

  @Test
  void deleteHouseMemberDocumentSuccess() throws Exception {
    // given
    addDefaultHouseMemberDocument();

    // when
    mockMvc.perform(delete(TESTING_ENDPOINT_URL, MEMBER_ID)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNoContent());

    // then
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    assertNull(member.getHouseMemberDocument());
  }

  @Test
  void deleteHouseMemberDocumentMemberNotExists() throws Exception {
    // given
    addDefaultHouseMemberDocument();

    // when
    mockMvc.perform(delete(TESTING_ENDPOINT_URL, "non-existing-member-id")
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void deleteHouseMemberDocumentNoDocumentPresent() throws Exception {
    mockMvc.perform(delete(TESTING_ENDPOINT_URL, MEMBER_ID)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void getHouseMemberDocumentNoDocumentPresent() throws Exception {
    mockMvc.perform(get(TESTING_ENDPOINT_URL, MEMBER_ID)
        .headers(getHttpEntityHeaders()))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  private void addDefaultHouseMemberDocument() throws IOException {
    byte[] imageBytes = getImageAsByteArray(10, 10);
    HouseMemberDocument houseMemberDocument =
        houseMemberDocumentRepository.save(new HouseMemberDocument(TEST_DOCUMENT_NAME, imageBytes));
    HouseMember member = houseMemberRepository.findByMemberId(MEMBER_ID).get();
    member.setHouseMemberDocument(houseMemberDocument);
    houseMemberRepository.save(member);
  }

  private void authDefaultUser() {
    if (userRepository.findByEmail(TEST_USER_EMAIL) == null) {
      CreateUserRequest createUserRequest =
          new CreateUserRequest(TEST_USERNAME, TEST_USER_EMAIL, TEST_USER_PASSWORD);
      sendRequest(HttpMethod.POST, "users", createUserRequest);
    }
    updateJwtToken(new LoginUserRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD));
  }

  private byte[] getImageAsByteArray(int height, int width) throws IOException {
    BufferedImage documentImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    try (ByteArrayOutputStream imageBytesStream = new ByteArrayOutputStream()) {
      ImageIO.write(documentImage, "jpg", imageBytesStream);
      return imageBytesStream.toByteArray();
    }
  }
}
