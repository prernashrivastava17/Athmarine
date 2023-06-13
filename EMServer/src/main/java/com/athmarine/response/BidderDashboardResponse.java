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
public class BidderDashboardResponse {

    private long liveStatus;

    private long newRequest;

    private long interestedRequest;

    private long bidsInProgress;

    private long raisedBids;

    private long purchaseOrderReceived;

    private long engineerAllocation;

    private long workInProgress;

    private long raisedAddCostDiscount;

    private long completedJobs;

    private long invoiceRaised;

}
