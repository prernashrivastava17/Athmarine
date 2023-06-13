package com.athmarine.response;


import com.athmarine.request.BidsModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceResponseModel {

    private BidsModel bidsModel;

    private String customerName;

    private String customerAddress;

    private String invoiceNumber;

    private String currency;

    private String voucherApplied;

    private Integer voucherDiscount;

    private Integer Total;
    
    private Integer serviceRequestId;
    
    private String poUid;
    
    private String invoiceStatus;
    
    

}

