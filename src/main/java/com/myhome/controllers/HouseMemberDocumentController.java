package com.myhome.controllers;


import com.myhome.domain.HouseMemberDocument;
import com.myhome.services.HouseMemberDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

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
            produces = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<byte[]> getHoseMemberDocument(@PathVariable String memberId) {
        log.trace("Received request to get house member documents");
        Optional<HouseMemberDocument> houseMemberDocumentOptional = houseMemberDocumentService.findHouseMemberDocument(memberId);

        if(houseMemberDocumentOptional.isPresent()) {

            HouseMemberDocument houseMemberDocument = houseMemberDocumentOptional.get();

            HttpHeaders headers = new HttpHeaders();
            byte[] content = houseMemberDocument.getDocument();

            headers.setCacheControl(CacheControl.noCache().getHeaderValue());
            headers.setContentType(MediaType.IMAGE_JPEG);

            ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                    .filename(houseMemberDocument.getDocumentFilename())
                    .build();

            headers.setContentDisposition(contentDisposition);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Add house member's documents",
            responses = {
                    @ApiResponse(responseCode = "204", description = "if document saved"),
                    @ApiResponse(responseCode = "409", description = "if document save error"),
                    @ApiResponse(responseCode = "413", description = "if document file too large")
            }
    )
    @PostMapping(
            path = "/members/{memberId}/documents",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Void> uploadHoseMemberDocument(
            @PathVariable String memberId, @RequestParam("memberDocument") MultipartFile memberDocument) throws IOException {
        log.trace("Received request to add house member documents");
        boolean isDocumentSaved = houseMemberDocumentService.createHouseMemberDocument(memberDocument, memberId);
        if(isDocumentSaved) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(description = "Update house member's documents",
            responses = {
                    @ApiResponse(responseCode = "204", description = "if document updated"),
                    @ApiResponse(responseCode = "409", description = "if document save error"),
                    @ApiResponse(responseCode = "413", description = "if document file too large")
            }
    )
    @PutMapping(
            path = "/members/{memberId}/documents",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Void> updateHoseMemberDocument(
            @PathVariable String memberId, @RequestParam("memberDocument") MultipartFile memberDocument) throws IOException {
        log.trace("Received request to update house member documents");
        boolean isDocumentSaved = houseMemberDocumentService.updateHouseMemberDocument(memberDocument, memberId);
        if(isDocumentSaved) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
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
        if(isDocumentDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
