package com.athmarine.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestBidsResponseModel {

    private Integer bidId;

    private Integer serviceRequestId;

    private String requestUID;

    private String bidUID;

    private Integer poId;

    private String poUID;

    private Integer invoiceId;

    private String vessel;

    private String category;

    private String country;

    private String port;

    private Date eta;

    private Date etd;

    private Date serviceRequestOn;

    private long remainingTime;

    private String invoiceStatus;

    private Integer invoiceValue;

    private String bidderName;

    private Integer proposedAmount;

    private String currency;

    private String status;

    private String declineReason;
}
