package com.athmarine.request;

import com.athmarine.entity.User;
import com.athmarine.entity.VendorApproverApprovedStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidderApproverModel {

	private Integer id;

	private Integer userId;

	private VendorApproverApprovedStatus isApprovedStatus;

	private int status;

	private User approvedBy;

}
