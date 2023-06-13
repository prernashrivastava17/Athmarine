package com.athmarine.response;

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
public class GetServiceRequestCount {

	private long invoiceRaised;

	private long invoicePaid;

	private long payables;

	private long vouchers;
	
	private Integer jobAssigned;
	
	private Integer jobInProgress;
	
	private Integer serviceReportUploaded;

	private Integer approved;

	private Integer toApprove;

}
