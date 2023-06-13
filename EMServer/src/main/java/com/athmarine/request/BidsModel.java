package com.athmarine.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidsModel {

	private Integer id;

	private Integer companyId;
	
	private String bidUid;

	private Integer bidderId;

	private Integer serviceRequestId;

	private Integer approverId;
	
	private boolean status;
	
	private String bidStatus;
	
	private Integer expectedHoursNeeded;
	
	private Integer noOfEngineer;
	
	private Integer warranty;
	
	private Integer totalProposedAmount;
	
	private Integer totalAmountGet;
	
	private Integer totalTax;

	private Integer totalAddCost;

	private Integer totalAddCostGet;
	
	private List<ProposedEngineers> proposedEngineers;

	private List<TravelCostVendorModel> travelCostVendorModel;

	private List<MiscellaneousVendorModel> miscellaneousVendorModel;

	private List<WorkingVendorModel> workingVendorModel;

}
