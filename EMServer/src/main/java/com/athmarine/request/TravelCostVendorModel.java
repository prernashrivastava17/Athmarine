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
public class TravelCostVendorModel {

	private String inline;

	private String comments;
	
	private Integer bidId;
	
	private Integer proposedAmount;
	
	private Integer amountYouGet;

	private Integer id;

	private Integer fareValue;

	private Integer approvedAmount;

	private Integer extraExpenses;

	private Integer extraExpensesYouGet;

	private String extraExpenseApprovalStatus;

}
