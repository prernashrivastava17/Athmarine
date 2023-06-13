package com.athmarine.response;

import com.athmarine.request.MiscellaneousCustomerModel;
import com.athmarine.request.SparesCustomerModel;
import com.athmarine.request.TravelCostCustomerModel;
import com.athmarine.request.WorkingCustomerModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetBidsQuotationResponse {

	private MiscellaneousCustomerModel miscellaneous;

	private SparesCustomerModel spares;

	private TravelCostCustomerModel travelCost;

	private WorkingCustomerModel working;
}
