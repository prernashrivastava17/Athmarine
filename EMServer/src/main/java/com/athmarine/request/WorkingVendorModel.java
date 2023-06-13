package com.athmarine.request;

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
public class WorkingVendorModel {
	
	private Integer id;
	
	private String inline;
	
	private Integer proposedAmount;
	
	private Integer amountYouGet;
	
	private String comments;
	 
	private Integer bidId;
	
	private Integer fareValue;

	private Integer approvedAmount;

	private Integer extraExpenses;

	private Integer extraExpensesYouGet;

	private String extraExpenseApprovalStatus;

}
