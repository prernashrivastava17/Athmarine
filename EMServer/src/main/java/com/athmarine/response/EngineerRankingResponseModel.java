package com.athmarine.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EngineerRankingResponseModel {

    private Integer id;

    private Integer engineerId;

    private Integer companyId;

    private String ranking;

    private String color;

    private String engineerName;
}
