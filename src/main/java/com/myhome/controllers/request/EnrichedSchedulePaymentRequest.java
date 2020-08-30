package com.myhome.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnrichedSchedulePaymentRequest {
  private String type;
  @NotBlank
  @Size(min = 5, max = 300, message = "Description should be between 5 and 300 characters")
  private String description;
  private boolean recurring;
  private BigDecimal charge;
  private String dueDate;
  private String adminId;
  private Long adminEntityId;
  private String adminName;
  private String adminEmail;
  private String adminEncryptedPassword;
  private String memberId;
  private Long memberEntityId;
  private String houseMemberDocumentName;
  private String houseMemberName;
  private String houseMemberHouseID;
}
