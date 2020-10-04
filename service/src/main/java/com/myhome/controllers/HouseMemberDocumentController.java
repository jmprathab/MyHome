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

package com.myhome.controllers;

import com.myhome.domain.HouseMemberDocument;
import com.myhome.services.HouseMemberDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller which provides endpoints for managing house member documents
 */
@RestController
@Slf4j
public class HouseMemberDocumentController {

  private final HouseMemberDocumentService houseMemberDocumentService;

  public HouseMemberDocumentController(HouseMemberDocumentService houseMemberDocumentService) {
    this.houseMemberDocumentService = houseMemberDocumentService;
  }

  @Operation(description = "Returns house member's documents",
      responses = {
          @ApiResponse(responseCode = "200", description = "if document present"),
          @ApiResponse(responseCode = "404", description = "if params are invalid")
      }
  )
  @GetMapping(
      path = "/members/{memberId}/documents",
      produces = {MediaType.IMAGE_JPEG_VALUE}
  )
  public ResponseEntity<byte[]> getHoseMemberDocument(@PathVariable String memberId) {
    log.trace("Received request to get house member documents");
    Optional<HouseMemberDocument> houseMemberDocumentOptional =
        houseMemberDocumentService.findHouseMemberDocument(memberId);

    return houseMemberDocumentOptional.map(document -> {

      HttpHeaders headers = new HttpHeaders();
      byte[] content = document.getDocumentContent();

      headers.setCacheControl(CacheControl.noCache().getHeaderValue());
      headers.setContentType(MediaType.IMAGE_JPEG);

      ContentDisposition contentDisposition = ContentDisposition
          .builder("inline")
          .filename(document.getDocumentFilename())
          .build();

      headers.setContentDisposition(contentDisposition);

      return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Operation(description = "Add house member's documents",
      responses = {
          @ApiResponse(responseCode = "204", description = "if document saved"),
          @ApiResponse(responseCode = "409", description = "if document save error"),
          @ApiResponse(responseCode = "413", description = "if document file too large"),
          @ApiResponse(responseCode = "404", description = "if params are invalid")
      }
  )
  @PostMapping(
      path = "/members/{memberId}/documents",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity uploadHoseMemberDocument(
      @PathVariable String memberId, @RequestParam("memberDocument") MultipartFile memberDocument) {
    log.trace("Received request to add house member documents");

    Optional<HouseMemberDocument> houseMemberDocumentOptional =
        houseMemberDocumentService.createHouseMemberDocument(memberDocument, memberId);
    return houseMemberDocumentOptional
        .map(document -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Operation(description = "Update house member's documents",
      responses = {
          @ApiResponse(responseCode = "204", description = "if document updated"),
          @ApiResponse(responseCode = "409", description = "if document update error"),
          @ApiResponse(responseCode = "413", description = "if document file too large"),
          @ApiResponse(responseCode = "404", description = "if params are invalid")
      }
  )
  @PutMapping(
      path = "/members/{memberId}/documents",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity updateHoseMemberDocument(
      @PathVariable String memberId, @RequestParam("memberDocument") MultipartFile memberDocument) {
    log.trace("Received request to update house member documents");
    Optional<HouseMemberDocument> houseMemberDocumentOptional =
        houseMemberDocumentService.updateHouseMemberDocument(memberDocument, memberId);
    return houseMemberDocumentOptional
        .map(document -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Operation(
      description = "Delete house member's documents",
      responses = {
          @ApiResponse(responseCode = "204", description = "id document deleted"),
          @ApiResponse(responseCode = "404", description = "if params are invalid")
      }
  )
  @DeleteMapping("/members/{memberId}/documents")
  public ResponseEntity<Void> deleteHoseMemberDocument(@PathVariable String memberId) {
    log.trace("Received request to delete house member documents");
    boolean isDocumentDeleted = houseMemberDocumentService.deleteHouseMemberDocument(memberId);
    if (isDocumentDeleted) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
