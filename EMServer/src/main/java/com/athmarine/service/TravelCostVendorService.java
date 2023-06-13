package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Bids;
import com.athmarine.entity.TravelCostVendor;
import com.athmarine.entity.User;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.TravelCostVendorException;
import com.athmarine.repository.TravelCostVendorRepository;
import com.athmarine.request.TravelCostVendorModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class TravelCostVendorService {

	@Autowired
	TravelCostVendorRepository travelCostVendorRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	BidsService bidsService;

	public TravelCostVendorModel createTravelCostVendor(TravelCostVendorModel model) {

		return convertToModel(travelCostVendorRepository.save(convertToEntity(model)));
	}
	
	public TravelCostVendor convertToEntity(TravelCostVendorModel model)
	{
		
		Bids bid=bidsService.findByIds(model.getBidId());
		
		return TravelCostVendor.builder()
				.id(model.getId())
				.inline(model.getInline())
				.bids(bid)
				.amountYouGet(model.getAmountYouGet())
				.proposedAmount(model.getProposedAmount())
				.comments(model.getComments())
				.fareValue(model.getFareValue())
				.extraExpenses(model.getExtraExpenses())
				.extraExpensesYouGet(model.getExtraExpensesYouGet())
				.approvedAmount(model.getApprovedAmount())
				.extraExpenseApprovalStatus(model.getExtraExpenseApprovalStatus())
				.build();
		
	}
	
	public TravelCostVendorModel convertToModel(TravelCostVendor entity)
	{
		
		
		return TravelCostVendorModel.builder()
				.id(entity.getId())
				.inline(entity.getInline())
				.amountYouGet(entity.getAmountYouGet())
				.proposedAmount(entity.getProposedAmount())
				.comments(entity.getComments())
				.bidId(entity.getBids().getId())
				.fareValue(entity.getFareValue())
				.extraExpenses(entity.getExtraExpenses())
				.extraExpensesYouGet(entity.getExtraExpensesYouGet())
				.approvedAmount(entity.getApprovedAmount())
				.build();
		
	}
	
//
//	public TravelCostVendorModel getVendorTravelCostById(Integer bidderId) {
//
//		User user = userDetailsServiceImpl.findByIds(bidderId);
//
//		return convertToModel(travelCostVendorRepository.getTravelCostVendorBybidderId(user)
//				.orElseThrow(() -> new InvalidInputException(AppConstant.ErrorTypes.BIDDER_ID_NULL_EXIST_ERROR,
//						AppConstant.ErrorCodes.BIDDER_ID_NULL_ERROR_CODE,
//						AppConstant.ErrorMessages.BIDDER_ID_NULL_NOT_EXIST)));
//	}
//
//	public TravelCostVendor findByIds(Integer id) {
//
//		if (id == null) {
//
//			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
//					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);
//
//		} else {
//			return travelCostVendorRepository.findById(id).orElseThrow(
//					() -> new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
//							AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE,
//							AppConstant.ErrorMessages.TRAVEL_COST_VENDOR_EXIST_MESSAGE));
//		}
//	}
//
//	public TravelCostVendorModel findById(Integer id) {
//		if (id == null) {
//			throw new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
//					AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
//		} else {
//			return convertToModel(travelCostVendorRepository.findById(id).orElseThrow(
//					() -> new TravelCostVendorException(AppConstant.ErrorTypes.TRAVEL_COST_VENDOR_EXIST_ERROR,
//							AppConstant.ErrorCodes.TRAVEL_COST_VENDOR_ERROR_CODE,
//							AppConstant.ErrorMessages.TRAVEL_COST_VENDOR_EXIST_MESSAGE)));
//		}
//	}
//
//	public TravelCostVendor convertToEntity(TravelCostVendorModel model) {
//
//		User bidderId = userDetailsServiceImpl.findByIds(model.getBidderId());
//
//		Integer tempInteger = model.getTravelTimeCost() * model.getTotalTravelTime();
//
//		Integer totalSum = model.getLumpSumCost() + model.getAirFare() + model.getTransportation() + tempInteger
//				+ model.getEngineerRatePerHour();
//
//		return TravelCostVendor.builder().id(model.getId()).lumpSumCost(model.getLumpSumCost())
//				.airFare(model.getAirFare()).transportation(model.getTransportation())
//				.engineerRatePerHour(model.getEngineerRatePerHour()).totalTravelTime(model.getTotalTravelTime())
//				.travelTimeCost(model.getTravelTimeCost()).totalSum(totalSum).bidderId(bidderId).build();
//
//	}
//
//	public TravelCostVendorModel convertToModel(TravelCostVendor entity) {
//
//		UserModel bidderId = userDetailsServiceImpl.findById(entity.getBidderId().getId());
//
//		Integer tempInteger = entity.getTravelTimeCost() * entity.getTotalTravelTime();
//
//		Integer totalSum = entity.getLumpSumCost() + entity.getAirFare() + entity.getTransportation() + tempInteger
//				+ entity.getEngineerRatePerHour();
//
//		return TravelCostVendorModel.builder().id(entity.getId()).lumpSumCost(entity.getLumpSumCost())
//				.airFare(entity.getAirFare()).transportation(entity.getTransportation())
//				.engineerRatePerHour(entity.getEngineerRatePerHour()).totalTravelTime(entity.getTotalTravelTime())
//				.travelTimeCost(entity.getTravelTimeCost()).totalSum(totalSum).bidderId(bidderId.getId()).build();
//	}

}
