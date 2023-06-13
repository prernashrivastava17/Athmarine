package com.athmarine.response;

import com.athmarine.entity.Bids;

import com.athmarine.request.BidsModel;
import com.athmarine.request.UserModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@JsonInclude(Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOrderResponse {

    private Integer id;

    private String poUID;

    private String customerPoNo;

    private UserModel customer;

    private UserModel vendor;

    private String vesselName;

    private String equipmentName;

    private String equipmentCategory;

    private Date serviceRequestOn;

    private String contactPerson;

    private String currency;

    private BidsModel bids;
    

    private Integer requestId;

}
