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
public class HistoryCounts {

    private long timedOut;

    private long notShownAnyInterest;

    private long bidsWithdrawn;

    private long poWithdraws;

}
