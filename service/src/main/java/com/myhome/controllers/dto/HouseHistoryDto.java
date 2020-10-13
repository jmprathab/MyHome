package com.myhome.controllers.dto;

import java.util.Date;
import lombok.Data;

@Data
public class HouseHistoryDto {
  private String memberId;
  private String houseId;
  private Date StayFromDate;
  private Date StayToDate;

}
