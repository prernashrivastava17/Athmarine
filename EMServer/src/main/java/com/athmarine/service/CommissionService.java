package com.athmarine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athmarine.entity.MiscellaneousCommission;
import com.athmarine.entity.SparesCommission;
import com.athmarine.entity.TravelCommission;
import com.athmarine.entity.WorkingCommission;
import com.athmarine.exception.ResourceNotFoundException;
import com.athmarine.repository.MiscellaneousCommissionRepository;
import com.athmarine.repository.SparesCommissionRepository;
import com.athmarine.repository.TravelCommissionRepository;
import com.athmarine.repository.WorkingCommissionRepository;
import com.athmarine.request.MiscellaneousCommissionModel;
import com.athmarine.request.SparesCommissionModel;
import com.athmarine.request.TravelCostCommissionModel;
import com.athmarine.request.WorkingCommissionModel;
import com.athmarine.resources.AppConstant;
import com.athmarine.response.CommissionResponseModel;
import com.sun.istack.NotNull;

@Service
public class CommissionService {

	@Autowired
	TravelCommissionRepository travelCommissionRepository;

	@Autowired
	SparesCommissionRepository sparesCommissionRepository;

	@Autowired
	WorkingCommissionRepository workingCommissionRepository;

	@Autowired
	MiscellaneousCommissionRepository miscellaneousCommissionRepository;

	public TravelCommission findByTravelCommissionIds(@NotNull Integer id) {
		return travelCommissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
	}

	public WorkingCommission findWorkingCommissionByIds(@NotNull Integer id) {
		return workingCommissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
	}

	public SparesCommission findSparesCommissionByIds(@NotNull Integer id) {
		return sparesCommissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
	}

	public MiscellaneousCommission findMiscellaneousCommissionByIds(@NotNull Integer id) {
		return miscellaneousCommissionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ErrorTypes.RESOURCE_NOT_FOUND_ERROR,
						AppConstant.ErrorCodes.RESOURCE_NOT_FOUND,
						AppConstant.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE));
	}

	public CommissionResponseModel getAllCommission() {
		return convertToCommonCommissionModel(findByTravelCommissionIds(1), findWorkingCommissionByIds(1),
				findSparesCommissionByIds(1), findMiscellaneousCommissionByIds(1));
	}

	public CommissionResponseModel convertToCommonCommissionModel(TravelCommission entityTravel,
			WorkingCommission entityWorking, SparesCommission entitySpare,
			MiscellaneousCommission entityMiscellaneous) {
		return CommissionResponseModel.builder().travelCommission(converToTravelCommissionModel(entityTravel))
				.sparesCommission(convertToSpareCommissionModel(entitySpare))
				.workingCommission(convertToWorkingCommissionModel(entityWorking))
				.miscellaneousCommission(convertToMiscellaneousCommissionModel(entityMiscellaneous)).build();
	}

	public TravelCostCommissionModel converToTravelCommissionModel(TravelCommission entity) {
		return TravelCostCommissionModel.builder().id(entity.getId()).airFareCommission(entity.getAirFareCommission())
				.lumpSumCommission(entity.getLumpSumCommission())
				.transportationCommission(entity.getTransportationCommission()).status(entity.getStatus())
				.totalSum(entity.getTotalSum()).travelTimeCostCommission(entity.getTravelTimeCostCommission()).build();
	}

	public WorkingCommissionModel convertToWorkingCommissionModel(WorkingCommission entity) {
		return WorkingCommissionModel.builder().id(entity.getId())
				.holidaysWorkingRateOT1TotalCommission(entity.getHolidaysWorkingRateOT1TotalCommission())
				.holidaysWorkingRateOT2TotalCommission(entity.getHolidaysWorkingRateOT2TotalCommission())
				.holidaysWorkingRateTotalCommission(entity.getHolidaysWorkingRateTotalCommission())
				.status(entity.getStatus()).workingRateOT1TotalCommission(entity.getWorkingRateOT1TotalCommission())
				.workingRateOT2TotalCommission(entity.getWorkingRateOT2TotalCommission())
				.workingRateTotalCommission(entity.getHolidaysWorkingRateTotalCommission()).build();
	}

	public SparesCommissionModel convertToSpareCommissionModel(SparesCommission entity) {
		return SparesCommissionModel.builder().id(entity.getId()).itemFiveCommission(entity.getItemFiveCommission())
				.itemFourCommission(entity.getItemFourCommission()).itemOneCommission(entity.getItemOneCommission())
				.itemThreeCommission(entity.getItemThreeCommission()).itemTwoCommission(entity.getItemTwoCommission())
				.status(entity.getStatus()).totalSum(entity.getTotalSum()).build();
	}

	public MiscellaneousCommissionModel convertToMiscellaneousCommissionModel(MiscellaneousCommission entity) {
		return MiscellaneousCommissionModel.builder().covidTestChargeCommission(entity.getCovidTestChargeCommission())
				.id(entity.getId()).otherChargeCommission(entity.getOtherChargeCommission())
				.portChargesCommission(entity.getPortChargesCommission())
				.shipyardChargesCommission(entity.getShipyardChargesCommission())
				.shipyardSurchargeCommission(entity.getShipyardSurchargeCommission()).status(entity.getStatus())
				.totalSum(entity.getTotalSum()).build();
	}
}
