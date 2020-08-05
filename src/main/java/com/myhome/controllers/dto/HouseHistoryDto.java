package com.myhome.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseHistoryDto {
    @NotNull
    private String houseId;
    @NotNull
    private String memberId;
    private Date fromDate;
    private Date toDate;
}
