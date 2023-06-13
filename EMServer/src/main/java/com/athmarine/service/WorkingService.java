package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Bids;
import com.athmarine.entity.User;
import com.athmarine.entity.WorkingVendor;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.TravelCostVendorException;
import com.athmarine.repository.WorkingVendorRepository;
import com.athmarine.request.UserModel;
import com.athmarine.request.WorkingVendorModel;
import com.athmarine.resources.AppConstant;

@Service
public class WorkingService {

	@Autowired
	WorkingVendorRepository workingVendorRepository;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	BidsService bidsService;

	public WorkingVendorModel createWorkingVendor(WorkingVendorModel model) {
		
		

		return convertToModels(workingVendorRepository.save(convertToEntities(model)));
	}
	
//	public WorkingVendorModel getWorkingVendorById(int bidderId) {
//	
//		User user = userDetailsServiceImpl.findByIds(bidderId);
//
//		return convertToModels(workingVendorRepository.getWorkingVendorBybidderId(user)
//				.orElseThrow(() -> new InvalidInputException(AppConstant.ErrorTypes.BIDDER_ID_NULL_EXIST_ERROR,
//						AppConstant.ErrorCodes.BIDDER_ID_NULL_ERROR_CODE,
//						AppConstant.ErrorMessages.BIDDER_ID_NULL_NOT_EXIST)));
//	}


	public WorkingVendor findByIds(Integer id) {

		if (id == null) {

			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);

		} else {
			return workingVendorRepository.findById(id).orElseThrow(
					() -> new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.TRAVEL_COST_VENDOR_EXIST_MESSAGE));
		}
	}

	public WorkingVendorModel findById(Integer id) {
		if (id == null) {
			throw new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
					AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
		} else {
			return convertToModels(workingVendorRepository.findById(id).orElseThrow(
					() -> new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
							AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE,
							AppConstant.ErrorMessages.TRAVEL_COST_VENDOR_EXIST_MESSAGE)));
		}
	}

//	public WorkingVendor convertToEntity(WorkingVendorModel model) {
//
//		User bidderId = userDetailsServiceImpl.findByIds(model.getBidderId());
//		
//		Integer sum = model.getWorkingRate() * model.getEstimatedWorkingHours();
//
//		Integer sumOTP1 = model.getWorkingRateOT1() * model.getEstimatedWorkingHours();
//
//		Integer totalSum = sum + sumOTP1 + model.getWorkingRateOT2();
//
//		return WorkingVendor.builder().id(model.getId()).workingRate(model.getWorkingRate()).bidderId(bidderId)
//				.workingRateOT1(model.getWorkingRateOT1()).workingRateOT2(model.getWorkingRateOT2())
//				.estimatedWorkingHours(model.getEstimatedWorkingHours())
//				.totalSum(totalSum).build();
//
//	}
//
//	public WorkingVendorModel convertToModel(WorkingVendor entity) {
//		
//		UserModel bidderId = userDetailsServiceImpl.findById(entity.getBidderId().getId());
//
//		Integer sum = entity.getWorkingRate() * entity.getEstimatedWorkingHours();
//
//		Integer sumOTP1 = entity.getWorkingRateOT1() * entity.getEstimatedWorkingHours();
//
//		Integer totalSum = sum + sumOTP1 + entity.getWorkingRateOT2();
//
//		return WorkingVendorModel.builder().id(entity.getId()).workingRate(entity.getWorkingRate()).bidderId(bidderId.getId())
//				.workingRateOT1(entity.getWorkingRateOT1()).workingRateOT2(entity.getWorkingRateOT2())
//				.estimatedWorkingHours(entity.getEstimatedWorkingHours())
//				.totalSum(totalSum).build();
//	}
	
	public WorkingVendor convertToEntities(WorkingVendorModel model)
	{
		
		Bids bid=bidsService.findByIds(model.getBidId());
		
		
		return WorkingVendor.builder()
				.id(model.getId())
				.inline(model.getInline())
				.comments(model.getComments())
				.bids(bid)
				.proposedAmount(model.getProposedAmount())
				.amountYouGet(model.getAmountYouGet())
				.fareValue(model.getFareValue())
				.extraExpenses(model.getExtraExpenses())
				.extraExpensesYouGet(model.getExtraExpensesYouGet())
				.approvedAmount(model.getApprovedAmount())
				.extraExpenseApprovalStatus(model.getExtraExpenseApprovalStatus())
				.build();
	}
	
	
	public WorkingVendorModel convertToModels(WorkingVendor entity)
	{
				
		return WorkingVendorModel.builder()
				.id(entity.getId())
				.inline(entity.getInline())
				.comments(entity.getComments())
				.proposedAmount(entity.getProposedAmount())
				.amountYouGet(entity.getAmountYouGet())
				.bidId(entity.getBids().getId())
				.fareValue(entity.getFareValue())
				.extraExpenses(entity.getExtraExpenses())
				.extraExpensesYouGet(entity.getExtraExpensesYouGet())
				.approvedAmount(entity.getApprovedAmount())
				.build();
	}

}
