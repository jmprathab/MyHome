package com.myhome.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

/**
 * This class is used to enrich the normal SchedulePaymentRequest with details relating to the admin and house member in order
 * to map to the User and HouseMember fields of payment successfully.
 * By doing this, you can avoid having to specify all the extra details in the request and just use the IDs
 * to get the data to enrich this request
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class EnrichedSchedulePaymentRequest extends SchedulePaymentRequest {
  private Long adminEntityId;
  private String adminName;
  private String adminEmail;
  private String adminEncryptedPassword;
  private Set<String> adminCommunityIds;
  private Long memberEntityId;
  private String houseMemberDocumentName;
  private String houseMemberName;
  private String houseMemberHouseID;

  public EnrichedSchedulePaymentRequest(String type, String description, boolean recurring, BigDecimal charge, String dueDate, String adminId, Long adminEntityId, String adminName, String adminEmail, String adminEncryptedPassword, Set<String> adminCommunityIds, String memberId, Long memberEntityId, String houseMemberDocumentName, String houseMemberName, String houseMemberHouseID) {
    super(type, description, recurring, charge, dueDate, adminId, memberId);
    this.adminName = adminName;
    this.adminEmail = adminEmail;
    this.adminEncryptedPassword = adminEncryptedPassword;
    this.adminCommunityIds = adminCommunityIds;
    this.adminEntityId = adminEntityId;
    this.memberEntityId = memberEntityId;
    this.houseMemberDocumentName = houseMemberDocumentName;
    this.houseMemberName = houseMemberName;
    this.houseMemberHouseID = houseMemberHouseID;
  }
}
