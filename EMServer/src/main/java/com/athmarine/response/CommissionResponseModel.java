package com.athmarine.response;

import com.athmarine.request.MiscellaneousCommissionModel;
import com.athmarine.request.SparesCommissionModel;
import com.athmarine.request.TravelCostCommissionModel;
import com.athmarine.request.WorkingCommissionModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommissionResponseModel {

	private TravelCostCommissionModel travelCommission;

	private WorkingCommissionModel workingCommission;

	private SparesCommissionModel sparesCommission;

	private MiscellaneousCommissionModel miscellaneousCommission;

}
