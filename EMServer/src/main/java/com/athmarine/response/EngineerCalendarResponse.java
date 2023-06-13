package com.athmarine.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class EngineerCalendarResponse {
    private Integer engineerId;

    private String engineerName;

    private Date serviceOn;

    private Date serviceEndOn;

    private String poNumber;

    private String vesselName;

    private String port;

    private String CustomerCompanyName;

    private String equipmentCategory;

    private String equipmentName;

    private String poValue;

}
