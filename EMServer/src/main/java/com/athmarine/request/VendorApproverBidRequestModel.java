package com.athmarine.request;

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
public class VendorApproverBidRequestModel {

    Integer approverId;

    Integer bidId;

    String bidStatus;

    String declineReason;

    Integer headId;

    private  BidsModel bidsModel;
}
