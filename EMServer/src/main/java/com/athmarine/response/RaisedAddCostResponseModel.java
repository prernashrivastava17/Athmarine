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
public class RaisedAddCostResponseModel {

    private Integer bidId;

    private Integer serviceRequestId;

    private String requestUID;

    private String bidUID;

    private Integer poId;

    private String poUID;

    private String vessel;

    private String category;

    private Date serviceRequestOn;

    private String RaisedBy;

    private String status;

}
