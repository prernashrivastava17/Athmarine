package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.Bids;
import com.athmarine.entity.MiscellaneousVendor;
import com.athmarine.entity.User;
import com.athmarine.exception.InvalidInputException;
import com.athmarine.exception.MiscellaneousVendorException;
import com.athmarine.repository.MiscellaneousVendorRepository;
import com.athmarine.request.MiscellaneousVendorModel;
import com.athmarine.request.UserModel;
import com.athmarine.resources.AppConstant;

@Service
public class MiscellaneousVendorService {

	@Autowired
	MiscellaneousVendorRepository miscellaneousVendorRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	BidsService bidsService;
	

	public MiscellaneousVendorModel createMiscellaneousVendor(MiscellaneousVendorModel model) {

		return convertToModel(miscellaneousVendorRepository.save(convertToEntity(model)));
	}
	
	public MiscellaneousVendor convertToEntity(MiscellaneousVendorModel model)
	{
		Bids bid= bidsService.findByIds(model.getBidId());

		return MiscellaneousVendor.builder()
				.id(model.getId())
				.inline(model.getInline())
				.bids(bid)
				.comments(model.getComments())
				.proposedAmount(model.getProposedAmount())
				.amountYouGet(model.getAmountYouGet())
				.fareValue(model.getFareValue())
				.extraExpenses(model.getExtraExpenses())
				.extraExpensesYouGet(model.getExtraExpensesYouGet())
				.approvedAmount(model.getApprovedAmount())
				.extraExpenseApprovalStatus(model.getExtraExpenseApprovalStatus())
				.build();
	}
	
	
	public MiscellaneousVendorModel convertToModel(MiscellaneousVendor entity)
	{

		return MiscellaneousVendorModel.builder()
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
//
//	public MiscellaneousVendorModel getMiscellaneousVendorById(Integer bidderId) {
//
//		User user = userDetailsServiceImpl.findByIds(bidderId);
//
//		return convertToModel(miscellaneousVendorRepository.getMiscellaneousVendorBybidderId(user)
//				.orElseThrow(() -> new InvalidInputException(AppConstant.ErrorTypes.BIDDER_ID_NULL_EXIST_ERROR,
//						AppConstant.ErrorCodes.BIDDER_ID_NULL_ERROR_CODE,
//						AppConstant.ErrorMessages.BIDDER_ID_NULL_NOT_EXIST)));
//	}
//
//	public MiscellaneousVendor findByIds(Integer id) {
//
//		if (id == null) {
//
//			throw new InvalidInputException(AppConstant.ErrorTypes.ID_NULL_EXIST_ERROR,
//					AppConstant.ErrorCodes.ID_NULL_ERROR_CODE, AppConstant.ErrorMessages.ID_NULL_NOT_EXIST);
//
//		}
//		return miscellaneousVendorRepository.findById(id).orElseThrow(
//				() -> new MiscellaneousVendorException(AppConstant.ErrorTypes.MISCELLANEOUS_VENDOR_EXIST_ERROR,
//						AppConstant.ErrorCodes.MISCELLANEOUS_VENDOR_ERROR_CODE,
//						AppConstant.ErrorMessages.MISCELLANEOUS_VENDOR_EXIST_MESSAGE));
//	}
//
//	public MiscellaneousVendorModel findById(Integer id) {
//		if (id == null) {
//			throw new MiscellaneousVendorException(AppConstant.ErrorTypes.MISCELLANEOUS_VENDOR_EXIST_ERROR,
//					AppConstant.ErrorCodes.MISCELLANEOUS_VENDOR_ERROR_CODE, AppConstant.ErrorMessages.ID_EMPTY_MESSAGE);
//		} else {
//			return convertToModel(miscellaneousVendorRepository.findById(id)
//					.orElseThrow(() -> new MiscellaneousVendorException(
//							AppConstant.ErrorTypes.MISCELLANEOUS_VENDOR_EXIST_ERROR,
//							AppConstant.ErrorCodes.MISCELLANEOUS_VENDOR_ERROR_CODE,
//							AppConstant.ErrorMessages.MISCELLANEOUS_VENDOR_EXIST_MESSAGE)));
//		}
//	}
//
//	public MiscellaneousVendor convertToEntity(MiscellaneousVendorModel model) {
//
//		User bidderId = userDetailsServiceImpl.findByIds(model.getBidderId());
//
//		Integer totalSum = model.getCovidTestCharge() + model.getCovidTestCharge() + model.getShipyardCharges()
//				+ model.getShipyardSurcharge() + model.getOtherCharge();
//
//		return MiscellaneousVendor.builder().id(model.getId()).portCharges(model.getCovidTestCharge())
//				.covidTestCharge(model.getCovidTestCharge()).shipyardCharges(model.getShipyardCharges())
//				.bidderId(bidderId).shipyardSurcharge(model.getShipyardSurcharge()).otherCharge(model.getOtherCharge())
//				.totalSum(totalSum).build();
//
//	}
//
//	public MiscellaneousVendorModel convertToModel(MiscellaneousVendor entity) {
//		UserModel bidderId = userDetailsServiceImpl.findById(entity.getBidderId().getId());
//
//		Integer totalSum = entity.getCovidTestCharge() + entity.getCovidTestCharge() + entity.getShipyardCharges()
//				+ entity.getShipyardSurcharge() + entity.getOtherCharge();
//
//		return MiscellaneousVendorModel.builder().id(entity.getId()).portCharges(entity.getCovidTestCharge())
//				.covidTestCharge(entity.getCovidTestCharge()).shipyardCharges(entity.getShipyardCharges())
//				.shipyardSurcharge(entity.getShipyardSurcharge()).otherCharge(entity.getOtherCharge())
//				.totalSum(totalSum).bidderId(bidderId.getId()).build();
//	}

}
