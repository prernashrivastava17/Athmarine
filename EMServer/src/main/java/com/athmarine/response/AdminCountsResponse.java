package com.athmarine.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCountsResponse {

    Integer activeUsers;

    Integer activeBidders;

    Integer activeApprovers;

    Integer activeEngineers;

    Integer activeFinancers;
}
