package com.myhome.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * This class is used to enrich the normal SchedulePaymentRequest with details relating to the admin and house member in order
 * to map to the User and HouseMember fields of payment successfully.
 * By doing this, you can avoid having to specify all the extra details in the request and just use the IDs
 * to get the data to enrich this request
 */
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
