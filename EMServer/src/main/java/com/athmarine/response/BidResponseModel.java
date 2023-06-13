package com.athmarine.response;

import java.util.List;

import com.athmarine.request.EngineerRatingResponseModel;
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
public class BidResponseModel {

	private Integer id;

	private SparesCustomerModel sparesCustomerModel;

	private TravelCostCustomerModel travelCostCustomerModel;

	private MiscellaneousCustomerModel miscellaneousCustomerModel;

	private WorkingCustomerModel workingCustomerModel;

	private String vendorLogo;

	private String vendorCompanyName;

	private FeedbackResponseModel vendorCompanyRating;

	//private FeedbackResponseModel vendorEngineerRating;
	
	private List<EngineerRatingResponseModel> engineerRatingResponseModel;

}
