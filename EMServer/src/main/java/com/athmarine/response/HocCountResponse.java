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
public class HocCountResponse {
	
	private Integer headId;
	
	private Integer purchageOrder;
	
	private Integer raisedBids;
	
	private Integer interestedRequest;
	
	private Integer invoicePaid;
	
	private Integer invoiceRaised;
	
	private Integer completedJobs;
	
	private Integer activeUsers;
	
	private Integer bidsApproval;
	
}
